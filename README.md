# Maze Q-Learning

### Introduction
Imagine we have an agent which only knows how to walk and where to go, but he doesn't know how to reach there. How can we teach him to find an optimal path to his destination ?
In this repository I've been using Q-Learning algorithm to solve this problem.
Q-Learning is a reinforcement learning technique (in simple words: learning by mistakes). The goal of Q-learning is to learn a policy, which tells the agent what action should take under what circumstances

### Problem
Imagine our agent is in a 2D envirement like below

![Maze-Problem](/assets/maze-problem.jpg)

Each blocks represent different state
* Red blocks represents wall which agent can't walk throgh it.
* Green block represents final state, the goal of agent is to find an optimal path to reach this state. 

Each state have its own reward value which is negative for wall states and positive for none-wall states.
At each block our agent can move with four actions: 
* Go left
* Go right
* Go up
* Go down

After each action he takes, it will takes his reward from envirement.
At first our agent randomly chooses an action to move to different states, but eventually he will learn by his mistakes and will find the optimal path to reach the final state.
