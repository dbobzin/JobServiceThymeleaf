package com.tutorial.securingwebtutorial.configurations;

import static java.lang.Math.min;

public class Test {
        public int findSmallestInt(int[] x) {
            int smallest = x[0]; // Initialize smallest with the first element of the array

            // Loop through the array starting from the second element
            for (int i = 1; i < x.length; i++) {
                // If the current element is smaller than the current smallest, update smallest
                if (x[i] < smallest) {
                    smallest = x[i];
                }
            }
            // Return the smallest element found
            return smallest;
        }

        public static void main(String[] args) {
            Test test = new Test();
            int[] array = {1, 2, 3};
            int smallestInt = test.findSmallestInt(array);
            System.out.println("The smallest integer in the array is: " + smallestInt);
        }
    }

