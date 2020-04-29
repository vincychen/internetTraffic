import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Sort implements Comparator<String> {
	private ArrayList<String> List;
	/**
	 * This is a constructor to sort a list
	 * @param dataList	 a list need to be sort
	 */
			
	public Sort(ArrayList<String> dataList) {
		this.List = dataList;
		
	}
	/**
	 * This is a method to sort the list and return
	 * @return ArrayList<String>  the list has been
	 * sorted
	 */
	public ArrayList<String> getSort() {
		Collections.sort(List,this);
		return List;
	}

	@Override
    public int compare(String left, String right) {
		ArrayList<Integer> left_address = new ArrayList<Integer>();
		ArrayList<Integer> right_address = new ArrayList<Integer>();
		for (String num : left.split("\\.")) {
			left_address.add(Integer.valueOf(num));
		}
		for (String num : right.split("\\.")) {
			right_address.add(Integer.valueOf(num));
		}
		for (int i = 0; i < 4; i++) {
			int n = left_address.get(i) - right_address.get(i);
			if (n != 0)
	    		return n;
		}
		return 0;
	}

}
