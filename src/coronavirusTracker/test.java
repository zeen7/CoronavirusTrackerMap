package coronavirusTracker;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a="bb,\n mm,aa";
		String[] split_data=a.split(",(?=\\S)|,(?=\\n)");
		System.out.println(split_data[1]);
	}

}
