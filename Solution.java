package nonogram;

import java.util.ArrayList;
import java.util.Random;

public class Solution implements Comparable<Solution> {
    
    private boolean [][] array;
    private int rNum, cNum;
    private Nonogram nonogram;
    int fitness = -1;
    int maxFitness = -1;

    public static final boolean ON = true;
    public static final boolean OFF = false;
    public static final int WEIGHT_RIGHT_SIZE = 10;
    public static final int WEIGHT_RIGHT_VALUE = 5;
    public static final int WEIGHT_RIGHT_TOTAL = 1;
    public static final double MUTATION_RATE = 0.07;

    public Solution(Nonogram nonogram){
        rNum = nonogram.getRowHeaders().size();
        cNum = nonogram.getColumnHeaders().size();
        this.nonogram = nonogram;

        array = new boolean[rNum][cNum];
        maxFitness = generateMaxFitness();

    }

    public void generateRandomSol(){
        Random ranGen = new Random();

        for(int r = 0; r < array.length; r++){
            for (int c =0; c < array.length; c++){
                array[r][c] = ranGen.nextBoolean();
            }
        }
    }

    public void mutate(){
        Random randGen = new Random();

        for(int r = 0; r < array.length; r++){
            if(randGen.nextDouble() < MUTATION_RATE){
              array[r] = mutateLine(array[r], getNonogram().getRowHeaders().get(r));
            }
        }

        for(int c = 0; c < array.length; c++){
            if(randGen.nextDouble() < MUTATION_RATE){
                setColumn(c, mutateLine(getCol(array, c), getNonogram().getColumnHeaders().get(c)));
            }
        }

    }

    private void setColumn(int c, boolean[] mutateLine){
        for(int i = 0; i < mutateLine.length; i++){
            array[i][c] = mutateLine[i];
        }
    }

    private boolean[] mutateLine(boolean[] bs, ArrayList<Integer> arrayList){
       // ArrayList<Integer> bsList = getTally(bs);
        int cellsNeeded = getSum(arrayList) + arrayList.size() - 1;
        int used = 0;
        int currentInRow = 0;
        int currentGroup = 0;

        //Create new bsList
        for(int i = 0; i < bs.length; i++){
            //if in the middle of a streak
            if(  i > 0 && bs[i-1]){
                //check if should continue
                if(currentInRow < arrayList.get(currentGroup)){
                    bs[i] = true;
                    currentInRow++;
                    used++;
                    //Or end the current streak and move on
                } else if(currentInRow == arrayList.get(currentGroup)){
                    bs[i] = false;
                    currentInRow = 0;
                    used++;
                    currentGroup++;
                }
            } else {

                double chanceNext = ((double)cellsNeeded - (double)used) / ((double)bs.length - (double)used);
                double rand = Math.random();

                if(rand < chanceNext){
                    //if chance of adding is correct
                    bs[i] = true;
                    currentInRow = 1;
                    used++;
                } else {
                    bs[i] = false;
                }

            }
        }

        return bs;
    }

    public Solution[] crossover(Solution solution, float random) {

        Solution[] offspring = new Solution[2];

        //intialize offspring
        for(int i = 0; i < offspring.length; i++){
            offspring[i] = new Solution(nonogram);
        }

        //copy parent chromosomes into offspring chromosomes
        //switch the parent-offspring matching when a random crossover point is reached

        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){

                if(random > (float) ((i + j * array.length) / (array.length * array[i].length))) {
                    offspring[0].getArray()[i][j] = this.array[i][j];
                    offspring[1].getArray()[i][j] = solution.getArray()[i][j];
                } else{
                    offspring[1].getArray()[i][j] = this.array[i][j];
                    offspring[0].getArray()[i][j] = solution.getArray()[i][j];
                }
            }
        }

        return offspring;
    }

    @Override
    public int compareTo(Solution solution){
        return this.fitness - solution.getFitness();
    }

    //calculate and return fitness of solution 
    public int evaluate(){
        fitness = 0;

        ArrayList<ArrayList<Integer>> row = nonogram.getRowHeaders();
        ArrayList<ArrayList<Integer>> col = nonogram.getColumnHeaders();

        //if looking at each row, then look at each row score
        for(int a = 0; a < array.length; a++){
            fitness += getScore(row.get(a), array[a]);
        }

        for(int b = 0; b < array[0].length; b++){
            fitness += getScore(col.get(b), getCol(array, b));
        }

        return fitness;
    }

    //get score of a specifi row or column
    private int getScore(ArrayList<Integer> arrayList, boolean[] bs){
        int score = 0;
        ArrayList<Integer> bsList = getTally(bs);

        //Reward for having right number of clumps
        if(arrayList.size() == bsList.size()){
            score += WEIGHT_RIGHT_SIZE;
        }

        //Reward for a correct total
        for(int a = 0; a < arrayList.size(); a++){
            if(getSum(bs) == getSum(arrayList)){
                score += WEIGHT_RIGHT_TOTAL;
            }
        }

        //Reward for having right size clumps
        for(int b = 0; b < arrayList.size(); b++){
            if( b < bsList.size()){
                score += (arrayList.get(b) - Math.abs(arrayList.get(b)
                 - bsList.get(b)))
                 * WEIGHT_RIGHT_VALUE;
            }
        }

        return score;
    }

    private int getSum(boolean[] bs){
        int sum = 0;
        for(boolean b : bs){
            if(b){
                sum++;
            }
        }

        return sum;
    }

    private int getSum(ArrayList<Integer> arrayList){
        int sum = 0;
        for(Integer i : arrayList){
            sum += i;
        }

        return sum;
    }

    //get column b of a boolean[][]
    private boolean[] getCol(boolean[][] pS, int b){
        boolean[] c = new boolean[pS[0].length];
        for(int a  = 0; a < pS[0].length; a++){
            c[a] = pS[a][b];
        }

        return c;
    }

    // Returns list of chunk sizes
	private ArrayList<Integer> getTally(boolean[] bs) {
		ArrayList<Integer> psList = new ArrayList();

		int streak = bs[0] == ON ? 1 : 0;
		for (int a = 1; a < bs.length; a++) {
			if (bs[a] == OFF) {
				if (streak > 0) {
					psList.add(streak);
					streak = 0;
				}
			} else {
				streak++;
			}
		}

		// If end of row/col is reached with active streak
		if (streak != 0) {
			psList.add(streak);
		}

		return psList;
	}


    public Nonogram getNonogram(){
        return nonogram;
    }
    public boolean[][] getArray(){
        return array;
    }
    public void setArray(boolean[][] array){
        this.array = array;
    }
    public int getFitness(){
        return fitness;
    }

    //Calculate and return fitness of solution
    public int generateMaxFitness(){
        int f = 0;

        ArrayList<ArrayList<Integer>> row = nonogram.getRowHeaders();
        ArrayList<ArrayList<Integer>> col = nonogram.getColumnHeaders();

        //Looking at each row/column  score
        for (int a = 0; a < array.length; a++){
            f += getMaxScore(row.get(a), array[a]);
        }

        for (int b = 0; b < array[0].length; b++){
            f += getMaxScore(col.get(b), getCol(array, b));
        }

        return f;
    }

    private int getMaxScore(ArrayList<Integer> arrayList, boolean [] bs){

        int score = 0;

        score += WEIGHT_RIGHT_SIZE;

        score += WEIGHT_RIGHT_TOTAL * arrayList.size();

        for (int b = 0; b < arrayList.size(); b++){
            
            score += arrayList.get(b) * WEIGHT_RIGHT_VALUE;

        }

        return score;
    }
    
    public boolean isComplete() {
		return getFitness() == getMaxFitness();
	}

	public int getMaxFitness() {
		return maxFitness;
	}
}




