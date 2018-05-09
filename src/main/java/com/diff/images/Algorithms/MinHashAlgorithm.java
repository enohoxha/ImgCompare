package com.diff.images.Algorithms;

import com.diff.images.Contracts.AlgorithmsContract;
import com.diff.images.Models.HashFunctionModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MinHashAlgorithm implements AlgorithmsContract{

    private static final int  number_of_hash_funtions=169;
    private HashFunctionModel[] hashFunctions;
    private int biggerSetLength;

    private int[] firstBigSet;
    private int[] secondBigSet;

    private int[] signatureFirstBigSet;
    private int[] signatureSecondBigSet;

    public MinHashAlgorithm(int[] firstBigSet, int[] secondBigSet) {

        this.firstBigSet = firstBigSet;
        this.secondBigSet = secondBigSet;
        this.biggerSetLength = firstBigSet.length > secondBigSet.length? firstBigSet.length : secondBigSet.length;

        signatureFirstBigSet = new int[number_of_hash_funtions];
        signatureSecondBigSet = new int[number_of_hash_funtions];
        Arrays.fill(signatureFirstBigSet,  Integer.MAX_VALUE);
        Arrays.fill(signatureSecondBigSet,  Integer.MAX_VALUE);
    }

    private void generateHashFunctions() {

        Random rand = new Random();
        hashFunctions = new HashFunctionModel[number_of_hash_funtions];
        int a = rand.nextInt(30)+1;
        int b = rand.nextInt(30)+1;
        int c = rand.nextInt(30)+1;

        for (int i = 0; i < number_of_hash_funtions; i++) {
            hashFunctions[i] = new HashFunctionModel(a, b, c);
        }

    }

    /**
     * Execute algorithm logic
     */
    public void executeAlgorithm() throws IOException {
        this.generateHashFunctions();

        // loop hash function to get signatures

        for (int i = 0; i < biggerSetLength; i++) {
            for (int j = 0; j < number_of_hash_funtions; j++) {

                hashFunctions[j].setX(i);

                if(hashFunctions[j].calculateHashFunction() < signatureFirstBigSet[j]){
                    signatureFirstBigSet[i] = hashFunctions[i].calculateHashFunction();
                }
            }
        }

    }

    /**
     * Get algorithm output
     *
     * @return
     */
    public <T> T getOutput() {
        return null;
    }

}
