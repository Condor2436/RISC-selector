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

    public void addUnit(){
        this.nums++;
    }
    public void addUnit(int x){
        this.nums+=x;
    }
    public void addUnit(Units u){
        this.nums+=u.getNums();
    }
    public void reduceUnit(Units u){
        this.nums -= u.getNums();
    }
    @Override
    public String toString() {
        return "Units{"+nums+"}";
    }
}
