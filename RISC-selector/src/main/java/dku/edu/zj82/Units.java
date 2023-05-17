package dku.edu.zj82;

public class Units {
    private int nums;

    public Units(int nums) {
        this.nums = nums;
    }

    public Units() {
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
    public boolean checkEnoughUnit(Units units){
        return units.getNums() <= this.nums;
    }

    @Override
    public String toString() {
        return "Units{"+nums+"}";
    }
}
