## Hi
You are going through a technical interview with us at TSB, how exciting!

The purpose of this exercise is to get you building something that solves an actual customer problem, lets you
think through it and start on it on your own, so that you can come in and walk us through your solution.

## Overview
For this test users have to do bank reconciliation, where bank transactions are matched against the corresponding 
accounting records (Invoices, Bills, etc.).

If an exact match is found, then the system will automatically suggest a match. There can also be
multiple accounting records which match to a single transaction, but if some combination of the
records sum to be an exact match, then the user has to find and select them themselves.
Surely there is a better way!

## Goals
If you run the application in this project you will see a single activity which displays a list of
accounting records and the total amount of a transaction at the top, there's not much else to it!

And that's where you come in:

You have two pieces of functionality to add. And we don't want you spending too much time on it, so
if you run out of time or get stuck then don't worry, we can talk through it together during the
interview.

1. Wire up the behaviour so that when an accounting record is selected, it is subtracted from the
   remaining total at the top.

2. When the activity opens initially, select a single transaction that matches remaining total
   automatically if any.


### Thought Experiment

We'd also like you to THINK ABOUT how we could achieve the following functionality:
When a subset of the accounting records sum to be an exact match of the remaining total, then
automatically select them. You don't need to write any code here. We will aim to explore your
thinking and design when you come in.

## Hints

As you go, mark down any questions or concerns which come up, we would love to hear them as you walk
us through the code during the upcoming interview.

You can change, delete or add classes, interfaces, layouts, tests and dependencies as you see fit.