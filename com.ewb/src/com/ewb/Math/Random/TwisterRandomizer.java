package com.ewb.Math.Random;

public class TwisterRandomizer {
    private int r250_index,r521_index;
    private int[] r250_buffer = new int[250],r521_buffer = new int[521];
    
    public static void main(String[] args) {
        TwisterRandomizer tw=new TwisterRandomizer();
        tw.test();
    }
    
    public void test() {
        System.out.println("testing ");
        int maxRand=0,currentRand=0;
        
        int[] integerCounts={0,0,0,0,0,0,0,0,0,0};
        int numSimulations=100000;
        System.out.println("iter "+numSimulations);
        
        for (int i=0; i<numSimulations; i++) {
            currentRand=getRandomInteger(10);
            integerCounts[currentRand]++;
        }
        for (int i=0; i<10; i++) System.out.println(i+" "+integerCounts[i]+" ("+(float)((float)integerCounts[i]/(float)numSimulations)+"%)" );
    }
    
    public TwisterRandomizer() {
        java.util.Random r = new java.util.Random();
        int i = 521;
        int mask1 = 1;
        int mask2 = 0xFFFFFFFF;
        while (i-- > 250) {
            r521_buffer[i] = r.nextInt();
        }
        while (i-- > 31) {
            r250_buffer[i] = r.nextInt();
            r521_buffer[i] = r.nextInt();
        }
        while (i-- > 0) {
            r250_buffer[i] = (r.nextInt() | mask1) & mask2;
            r521_buffer[i] = (r.nextInt() | mask1) & mask2;
            mask2 ^= mask1;
            mask1 >>= 1;
        }
        r250_buffer[0] = mask1;
        r521_buffer[0] = mask2;
        r250_index = 0;
        r521_index = 0;
    }
    
    public int random() {
        //from -2147483647 to 2147483647
        int i1 = r250_index;
        int i2 = r521_index;
        int j1 = i1 - (250-103);
        if (j1 < 0) j1 = i1 + 103;
        int j2 = i2 - (521-168);
        if (j2 < 0) j2 = i2 + 168;
        int r = r250_buffer[j1] ^ r250_buffer[i1];
        r250_buffer[i1] = r;
        int s = r521_buffer[j2] ^ r521_buffer[i2];
        r521_buffer[i2] = s;
        i1 = (i1 != 249) ? (i1 + 1) : 0;
        r250_index = i1;
        i2 = (i2 != 521) ? (i2 + 1) : 0;
        r521_index = i2;
        return r ^ s;
    }
    
    public static int getRandomInteger(int n) {
        float r2=0f;
        boolean tryAgain=true;
        while (tryAgain) {
            try {
                TwisterRandomizer tw=new TwisterRandomizer();
                int r=tw.random();
                tryAgain=false;
                r2=r/2147483647f; // now between -1 and 1
                r2=r2/2f+.5f; // now between 0 and 1
                r2=r2*n;
                //r=(int) ((float)(r/2147483647f*(n))/2f)+(n/2);
            } catch (Exception e) {
                e.printStackTrace();
                tryAgain=true;
            }
        }
        return (int)r2;
    }
}