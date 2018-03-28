# mdpAgents.py
# parsons/20-nov-2017
#
# Version 1
#
# The starting point for CW2.
#
# Intended to work with the PacMan AI projects from:
#
# http://ai.berkeley.edu/
#
# These use a simple API that allow us to control Pacman's interaction with
# the environment adding a layer on top of the AI Berkeley code.
#
# As required by the licensing agreement for the PacMan AI we have:
#
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).

# The agent here is was written by Simon Parsons, based on the code in
# pacmanAgents.py

from pacman import Directions
from game import Agent
import api
import time
import csv


class Grid:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.grid = []
        subgrid = [] #empty placeholder list to populate with values for the map
        for i in range(self.height): #creates a number of placeholder rows equal to the height of the map
            row = []
            for j in range(self.width):#populates each row list with a number of 0s equal to width of the map
                row.append(0)
            subgrid.append(row) #adds the row of 0s to the subgrid variable

        self.grid = subgrid #sets the subgrid to the grid attribute for the grid class

    '''
    prints an easy to read visual representation of the expected utility for each coodrinate in the grid
    '''
    def prettyDisplay(self):
        for i in range(self.height):
            for j in range(self.width): #iterates through each coordinate in the grid
                print format(self.grid[self.height - (i + 1)][j], ".2f"), #prints the array of lists upside down and each number only up to 2 decimal places
            print
        print

    '''
    set method which sets the value in at the coordinate (i, j) to the given parameter value
    '''
    def setValue(self, x, y, value):
        self.grid[y][x] = value

    '''
    get method which returns the value in at the coordinate (i, j)
    '''
    def getValue(self, x, y):
        return self.grid[y][x]

    '''
    returns the height of the map as an int
    '''
    def getHeight(self):
        return self.height

    '''
    returns the width of the map as an int
    '''
    def getWidth(self):
        return self.width


class MDPAgent(Agent):
    def __init__(self):
        self.counter = 0
        self.times = []

    '''    
    creates an instance of the Grid class to create a map based on the current layout used
    '''
    def makeMap(self, corners):
        height = self.getLayoutHeight(corners)
        width = self.getLayoutWidth(corners)
        self.map = Grid(width, height) #creates a grid object based on the height and width of the layout

    '''
    calculates the height of the layout
    '''
    def getLayoutHeight(self, corners):
        height = -1
        for i in range(len(corners)):
            if corners[i][1] > height:
                height = corners[i][1]
        return height + 1

    '''
    calculates the width of the layout
    '''
    def getLayoutWidth(self, corners):
        width = -1
        for i in range(len(corners)):
            if corners[i][0] > width:
                width = corners[i][0]
        return width + 1

    '''
    picks the optimal direction to move in based on the expected utility of each direction
    the best direction to move is returned
    '''
    def pickBest(self, map, loc):
        i = loc[0] #sets i to the current x value of pacman
        j = loc[1] #sets j to the current y value of pacman
        x = self.getExpectations(map, i, j)
        best = max(x) #sets best to the highest expectation value
        bestIndex = x.index(best) #gives the index in the x list of the greatest value
        #using the index, I can find out which direction produced the greatest expectation and return it
        if bestIndex == 0:
            return Directions.NORTH
        elif bestIndex == 1:
            return Directions.EAST
        elif bestIndex == 2:
            return Directions.SOUTH
        elif bestIndex == 3:
            return Directions.WEST

    '''
    returns a list of expectations for the 4 possible directions
    0.8 is used for the probability of moving in the direction that pacman intends to move in as there is a 80% chance this will happen
    0.1 is used for the other 2 directions that pacman maybe instead move in as there is a 10% chance this will happen
    '''
    def getExpectations(self, map, i, j):
        expectations = [] #creates an empty list which will be populated with expectations
        northExp = 0.8*Grid.getValue(map,i,j+1) + 0.1*Grid.getValue(map,i-1,j) + 0.1*Grid.getValue(map,i+1,j) #calculates the expectation of moving north
        eastExp = 0.8*Grid.getValue(map,i+1,j) + 0.1*Grid.getValue(map,i,j+1) + 0.1*Grid.getValue(map,i,j-1) #calculates the expectation of moving west
        southExp = 0.8*Grid.getValue(map, i,j-1) + 0.1*Grid.getValue(map,i-1,j)+ 0.1*Grid.getValue(map,i+1,j) #calculates the expectation of moving south
        westExp = 0.8*Grid.getValue(map,i-1,j) + 0.1*Grid.getValue(map,i,j+1) + 0.1*Grid.getValue(map,i,j-1) #calculates the expectation of moving west
        expectations.append(northExp)
        expectations.append(eastExp)
        expectations.append(southExp)
        expectations.append(westExp)

        self.expectationDisplay(northExp, eastExp, southExp, westExp) #passes the expectation values to the method which sets the expectation display values

        return expectations #returns the list of expectations

    '''
    runs bellmans iterative formula except the outer walls
    takes values from the previous map and sets the current map to the new values
    running condition checks for each possible coordinate is more efficient for finding empty spaces than running for loops for a list of locations for each item in the map
    	by deduction any coordinate which does not satisfy any of the if statements other than the last one must be an empty space
    '''
    def bellman(self, map, g, food, ghosts, walls, caps):
        for i in range(1, Grid.getWidth(map) - 1):
            for j in range(1, Grid.getHeight(map) - 1): #for each coordinate except the outer walls
                if ((i, j), 0) in ghosts: #all ghosts that can eat pacman
                    Grid.setValue(self.map, i, j, -100)  #terminal state. no need to calculate bellmans
                elif ((i, j), 1) in ghosts: #all ghosts that are afraid/edible
                    bell = 50 + g * max(self.getExpectations(map, i, j)) #returns a bellman iteration with R(s)=50 and gamme=g
                    Grid.setValue(self.map, i, j, bell)  #terminal state. no need to calculate bellmans
                elif (i, j) in caps: #all capsules in the map
                    bell = 3 + g * max(self.getExpectations(map, i, j)) #returns a bellman iteration with R(s)=3 and gamme=g
                    Grid.setValue(self.map, i, j, bell) #sets the value of map to the bellman result
                elif (i, j) in food: #all food in the map
                    bell = 3 + g * max(self.getExpectations(map, i, j))#returns a bellman iteration with R(s)=3 and gamme=g
                    Grid.setValue(self.map, i, j, bell)#sets the value of map to the bellman result
                elif (i, j) not in walls: #empty spaces in the map
                    bell = 0 + g * max(self.getExpectations(map, i, j))#returns a bellman iteration with R(s)=0 and gamme=g
                    Grid.setValue(self.map, i, j, bell)#sets the value of map to the bellman result
    '''
    this method sets the values of the expectations pacman sees to the corresponding values of the map which displays these values in the console
    '''
    def expectationDisplay(self, north, east, south, west):
        self.nearPacman = Grid(3, 3)
        Grid.setValue(self.nearPacman, 0, 0, "   ") #empty space for the diagonal values
        Grid.setValue(self.nearPacman, 0, 1, format(south, ".2f")) #expectations are set to two decimal places so the printed grid is clear
        Grid.setValue(self.nearPacman, 0, 2, "   ")
        Grid.setValue(self.nearPacman, 1, 0, format(west, ".2f"))
        Grid.setValue(self.nearPacman, 1, 1, " O ") #pacman location relative to expectations is shown as an O
        Grid.setValue(self.nearPacman, 1, 2, format(east, ".2f"))
        Grid.setValue(self.nearPacman, 2, 0, "   ")
        Grid.setValue(self.nearPacman, 2, 1, format(north, ".2f"))
        Grid.setValue(self.nearPacman, 2, 2, "   ")

    '''
    the method that correctly prints the grid which displays the expectations
    '''
    def printExpectationDisplay(self):
        for i in range(3):
            for j in range(3):  # iterates through each coordinate in the grid
                print Grid.getValue(self.nearPacman,3 - (i + 1), j),# prints the array of lists upside down and each number
            print
        print

    '''
    creates a csv file for the program to write test data to
    '''
    def openCSV(self):
        file = open("times.csv", "w") #writes to a new file csv called "times"
        with file:
            headings = ["step", "time"] #sets headings of the csv file
            writer = csv.DictWriter(file, fieldnames=headings) #uses a dictionary to write values under the correct headers
            writer.writeheader() #writes table headings to the file
    '''
    writes data, n, to a csv file
    '''
    def writeCSV(self, n):
        file = open("times.csv", "a") #opens the existing times file and appends it with new data
        with file:
            headings = ["step", "time"]
            writer = csv.DictWriter(file, fieldnames=headings)
            writer.writerow({"step": self.counter, "time": n})
            #writes the value of the counter (the step) to the file and the correpsonding variable of interest's value (which is passed to the method) under the second heading


    def getAction(self, state):
        corners = api.corners(state)
        walls = api.walls(state)
        food = api.food(state)
        location = api.whereAmI(state)
        legal = api.legalActions(state)
        ghosts = api.ghostStates(state)
        caps = api.capsules(state)

        #t0 = time.time()

        if self.counter == 0: #if this is the first time the getAction method has been ran then...
            self.makeMap(corners) #creates a map
            for i in range(10): #does five iterations of bellmans
                oldMap = self.map #saves the value of the previous expected utilities of the map
                self.bellman(oldMap, 0.9, food, ghosts, walls, caps) #runs bellmans on the values of the previous iteration of the map
            self.counter = self.counter + 1  # increases global counter variable to let the program know it has been ran before
        else: #if this isn't the first time the program has been
            for i in range(10):  # does ten iterations of bellmans
                oldMap = self.map  # saves the value of the previous expected utilities of the map
                self.bellman(oldMap, 0.9, food, ghosts, walls, caps)  # runs bellmans on the values of the previous iteration of the map

        #t1 = time.time()
        #delta = t1-t0

        #Grid.prettyDisplay(self.map) #prints a representation of the map to the console with the expected utility of each
        self.printExpectationDisplay() #prints the expectation values for the locations north, east, south and west of pacman's location

        best = self.pickBest(self.map, location) #returns the optimal direction to move in to

        if Directions.STOP in legal:
            legal.remove(Directions.STOP) #removes STOP from legal so that packman does not stop

        return api.makeMove(best, legal) #makes the optimal move
                                                                                                            