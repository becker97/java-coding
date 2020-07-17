/**
 *
 * Student Name: Lucas Becker Ferreira
 * 
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Sorting {
	public static void main(String[] args) {
		// Test here

		//creating two arrays of size 10000000 and populating them with random numbers
		int data[] = new int[10000000];
	 	int data2[] = new int[10000000];
		for(int j = 0; j < data.length; j++) {
			data[j] = (int)(Math.random()*100000);
			data2[j] = data[j];
		}

		//calculating the running time when using Merge Sort in seconds
		double startTime1 = System.nanoTime();
		mergeSort(data, 0, 10000000);
		double endTime1   = System.nanoTime();
		//dividing it by 1 billion to get answer in seconds
		double totalTime1 = (endTime1 - startTime1)/1000000000;
		System.out.printf("Merge Sort Running Time:  %.3f%n", totalTime1);

		//calculating the running time when using MergeInsertion sort in seconds
		double startTime2 = System.nanoTime();
		ForkJoinPool fpool = new ForkJoinPool();
		fpool.invoke(new mergeInsertion(data2, 0, data2.length));
		double endTime2 = System.nanoTime();
		double totalTime2 = (endTime2 - startTime2)/1000000000;
		System.out.printf("MergeInsertion Running Time: %.3f%n", totalTime2);

		//calculating the difference between both sorting algorithms
		System.out.println("MergeInsertion is " + (totalTime1 / totalTime2) + " times quicker than Merge Sort");

	}

	private static void insertionSort(int dt[], int a, int b){
		for(int i = a; i < b; i++){
			int j = i;
			while(j > a && dt[j] < dt[j-1]){
				int temp = dt[j]; dt[j] = dt[j-1]; dt[j-1] = temp;
				j--;
			}
		}
	}

	private static void mergeSort(int f[], int lb, int ub){
		//termination reached when a segment of size 1 reached - lb+1 = ub
		if(lb+1 < ub){
			int mid = (lb+ub)/2;
			mergeSort(f,lb,mid);
			mergeSort(f,mid,ub);
			merge(f,lb,mid,ub);
		}
	}

	static void merge(int f[], int p, int q, int r){
		//p<=q<=r
		int i = p; int j = q;
		//use temp array to store merged sub-sequence
		int temp[] = new int[r-p]; int t = 0;
		while(i < q && j < r){
			if(f[i] <= f[j]){
				temp[t]=f[i];i++;t++;
			}
			else{
				temp[t] = f[j]; j++; t++;
			}
		}
		//tag on remaining sequence
		while(i < q){ temp[t]=f[i];i++;t++;}
		while(j < r){ temp[t] = f[j]; j++; t++;}
		//copy temp back to f
		i = p; t = 0;
		while(t < temp.length){ f[i] = temp[t]; i++; t++;}
	}
}

//class that will mix Merge and Insertion Sorting algorithms
class mergeInsertion extends RecursiveAction{
	private int lb;
	private int ub;
	private int[] data;

	public mergeInsertion(int data1[], int lb, int ub) {
		this.data = data1;
		this.lb = lb;
		this.ub = ub;
	}
	
	//limiting the Merge Sorting segments
	protected void compute() {
		if(ub - lb < 100){
			insertionSort(data, lb, ub);
		}
		else {
			int middle = (lb + ub) / 2;
			mergeInsertion left = new mergeInsertion(data, lb, middle);
			mergeInsertion right = new mergeInsertion(data, middle, ub);
			
			invokeAll(left,right);
			left.join();
			right.join();
			merge(data, lb, middle, ub);
		}
	}

	//class that will do the merging
	static void merge(int f[], int p, int q, int r){
		//p<=q<=r
		int i = p; int j = q;
		//use temp array to store merged sub-sequence
		int temp[] = new int[r-p]; int t = 0;
		while(i < q && j < r){
			if(f[i] <= f[j]){
				temp[t]=f[i];i++;t++;
			}
			else{
				temp[t] = f[j]; j++; t++;
			}
		}
		//tag on remaining sequence
		while(i < q){ temp[t]=f[i];i++;t++;}
		while(j < r){ temp[t] = f[j]; j++; t++;}
		//copy temp back to f
		i = p; t = 0;
		while(t < temp.length){ f[i] = temp[t]; i++; t++;}
	}

	public void insertionSort(int dt[], int a, int b){
		for(int i = a; i < b; i++){
			int j = i;
			while(j > a && dt[j] < dt[j-1]){
				int temp = dt[j]; dt[j] = dt[j-1]; dt[j-1] = temp;
				j--;
			}
		}
	}
}