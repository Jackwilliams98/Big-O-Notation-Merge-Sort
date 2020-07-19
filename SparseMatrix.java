
/* C1647731
 *
 * Optionally, if you have any comments regarding your submission, put them here. 
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.io.*;

// A class that represents a dense vector and allows you to read/write its elements
class DenseVector {
	private int[] elements;

	public DenseVector(int n) {
		elements = new int[n];
	}

	public DenseVector(String filename) {
		File file = new File(filename);
		ArrayList<Integer> values = new ArrayList<Integer>();

		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextInt()) {
				values.add(sc.nextInt());
			}

			sc.close();

			elements = new int[values.size()];
			for (int i = 0; i < values.size(); ++i) {
				elements[i] = values.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read an element of the vector
	public int getElement(int idx) {
		return elements[idx];
	}

	// Modify an element of the vector
	public void setElement(int idx, int value) {
		elements[idx] = value;
	}

	// Return the number of elements
	public int size() {
		return (elements == null) ? 0 : (elements.length);
	}

	// Print all the elements
	public void print() {
		if (elements == null) {
			return;
		}

		for (int i = 0; i < elements.length; ++i) {
			System.out.println(elements[i]);
		}
	}
}

// A class that represents a sparse matrix
public class SparseMatrix {
	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
		System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
		System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
		System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
		System.err.println(" - Matrix-vector multiplication: java SparseMatrix -v <MatrixFile> <VectorFile>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			printCommandError();
			System.exit(-1);
		}

		if (args[0].equals("-i")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			System.out.println("The matrix has " + mat.getNumRows() + " rows and " + mat.getNumColumns() + " columns");
			System.out.println("It has " + mat.numNonZeros() + " non-zeros");
		} else if (args[0].equals("-r")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
		} else if (args[0].equals("-t")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			SparseMatrix transpose_mat = mat.transpose();
			System.out.println();
			System.out.println("Matrix elements:");
			mat.print();
			System.out.println();
			System.out.println("Transposed matrix elements:");
			transpose_mat.print();
		} else if (args[0].equals("-a")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat1 = new SparseMatrix();
			mat1.loadEntries(args[1]);
			System.out.println("Read matrix 1 from " + args[1]);
			System.out.println("Matrix elements:");
			mat1.print();

			System.out.println();
			SparseMatrix mat2 = new SparseMatrix();
			mat2.loadEntries(args[2]);
			System.out.println("Read matrix 2 from " + args[2]);
			System.out.println("Matrix elements:");
			mat2.print();
			SparseMatrix mat_sum1 = mat1.add(mat2);

			System.out.println();
			mat1.multiplyBy(2);
			SparseMatrix mat_sum2 = mat1.add(mat2);

			mat1.multiplyBy(5);
			SparseMatrix mat_sum3 = mat1.add(mat2);

			System.out.println("Matrix1 + Matrix2 =");
			mat_sum1.print();
			System.out.println();

			System.out.println("Matrix1 * 2 + Matrix2 =");
			mat_sum2.print();
			System.out.println();

			System.out.println("Matrix1 * 10 + Matrix2 =");
			mat_sum3.print();
		} else if (args[0].equals("-v")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			DenseVector vec = new DenseVector(args[2]);
			DenseVector mv = mat.multiply(vec);

			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
			System.out.println();

			System.out.println("Read vector from " + args[2] + ":");
			vec.print();
			System.out.println();

			System.out.println("Matrix-vector multiplication:");
			mv.print();
		}
	}

	// Loading matrix entries from a text file
	// You need to complete this implementation
	public void loadEntries(String filename) {
		File file = new File(filename);

		try {
			Scanner sc = new Scanner(file);
			numRows = sc.nextInt();
			numCols = sc.nextInt();
			entries = new ArrayList<Entry>();

			while (sc.hasNextInt()) {
				// Read the row index, column index, and value of an element
				int row = sc.nextInt();
				int col = sc.nextInt();
				int val = sc.nextInt();

				// Add your code here to add the element into data member entries

				int position = col + numCols*row; 		//Find the position 
				Entry positionValue = new Entry(position, val); //Add a new Entry
				entries.add(positionValue);		//Add the new Entry into the ArrayList
			}

			// Add your code here for sorting non-zero elements

			entries = mergeSort(entries); //Sort the entries

		} catch (Exception e) {
			e.printStackTrace();
			numRows = 0;
			numCols = 0;
			entries = null;
		}
	}

	public static ArrayList<Entry> mergeSort(ArrayList<Entry> entries) {
		ArrayList<Entry> left = new ArrayList<Entry>();		//Create both sides of ArrayList to add into
		ArrayList<Entry> right = new ArrayList<Entry>();
		int center;
		int n = entries.size();
	 
	    if (entries.size() <= 1) {    
	        return entries;
	    } else {
	        center = n/2;
	        for (int i=0; i<center; i++) {	//Copy the left half of a into the left
	                left.add(entries.get(i));
	        }
	        for (int i=center; i<n; i++) {	//copy the right half of a into the new arraylist
	                right.add(entries.get(i));
	        }
	        left  = mergeSort(left); //Sort the left and right halves of the arraylist
	        right = mergeSort(right);
	 
	        merge(left, right, entries); //Merge the results back together
	    }
	    return entries;
	}

    private static void merge(ArrayList<Entry> left, ArrayList<Entry> right, ArrayList<Entry> entries) {
		int indexLeft = 0; int indexRight = 0; int indexMerge = 0;
		ArrayList<Entry> temp = new ArrayList<Entry>();

		//While the arrays are still full, keep sorting and adding
		while (indexLeft < left.size() && indexRight < right.size()) {
			if (left.get(indexLeft).getPosition() < (right.get(indexRight).getPosition())) {
				entries.set(indexMerge, left.get(indexLeft));
				indexLeft++;
				indexMerge++;
			} else {
				entries.set(indexMerge, right.get(indexRight));
				indexRight++;
				indexMerge++;
			}
		}

    	int indexTemp;
    	if (indexLeft >= left.size()) {	//The left ArrayList has been use up
        	temp = right;
        	indexTemp = indexRight;
    	} else { //The right ArrayList has been used up
        	temp = left;
        	indexTemp = indexLeft;
    }
 
    	//Copy the rest of whichever ArrayList (left or right) was not used up
    	for (int i=indexTemp; i<temp.size(); i++) {
        	entries.set(indexMerge, temp.get(i));
      	 	indexMerge++;
    	}
	}

	// Default constructor
	public SparseMatrix() {
		numRows = 0;
		numCols = 0;
		entries = null;
	}

	// A class representing a pair of column index and elements
	private class Entry {
		private int position; // Position within row-major full array representation
		private int value; // Element value

		// Constructor using the column index and the element value
		public Entry(int pos, int val) {
			this.position = pos;
			this.value = val;
		}

		// Copy constructor
		public Entry(Entry entry) {
			this(entry.position, entry.value);
		}

		// Read column index
		int getPosition() {
			return position;
		}

		// Set column index
		void setPosition(int pos) {
			this.position = pos;
		}

		// Read element value
		int getValue() {
			return value;
		}

		// Set element value
		void setValue(int val) {
			this.value = val;
		}
	}

	// Adding two matrices
	public SparseMatrix add(SparseMatrix M) {
		// Add your code here

		//Create new ArrayList and Matrix and SparseMatrix
		ArrayList<Entry> addedEntries = new ArrayList<Entry>();
		SparseMatrix addedMatrix = new SparseMatrix();
		SparseMatrix N = new SparseMatrix();
		N.entries = entries;

		int val1; int val2; int currentPosition; int sum;
		int mCount = 0; int nCount = 0;

		//While the counts are less than the size of the matrices
		while (mCount < M.entries.size() || nCount < N.entries.size()) {
			if (mCount >= M.entries.size() ) { //If the count is bigger than the matrix size, add from the other
				val1 = N.entries.get(nCount).getValue();
				val2 = 0;
				currentPosition = N.entries.get(nCount).getPosition();
				nCount += 1;
			} else if (nCount >= N.entries.size()) {
				val1 = M.entries.get(mCount).getValue();
				val2 = 0;
				currentPosition = M.entries.get(mCount).getPosition();
				mCount += 1;
			} else {
				//Finding if the positions are the same, smaller or bigger and adding the correct value
				if (M.entries.get(mCount).getPosition() == N.entries.get(nCount).getPosition()) {
					val1 = M.entries.get(mCount).getValue();
					val2 = N.entries.get(nCount).getValue();
					currentPosition = M.entries.get(mCount).getPosition();
					mCount += 1;
					nCount += 1;
				} else if (M.entries.get(mCount).getPosition() > N.entries.get(nCount).getPosition()) {
					val1 = N.entries.get(nCount).getValue();
					val2 = 0;
					currentPosition = N.entries.get(nCount).getPosition();
					nCount += 1;
				} else {
					val1 = M.entries.get(mCount).getValue();
					val2 = 0;
					currentPosition = M.entries.get(mCount).getPosition();
					mCount += 1;
				}
			}
			//add the values and then add it to the ArrayList
			sum = val1+val2;
			Entry elements = new Entry(currentPosition, sum);
			addedEntries.add(elements);
		}

		//Setting the matrix at the correct size 
		addedMatrix.numCols = numCols;
		addedMatrix.numRows = numRows;
		addedMatrix.entries = addedEntries;
		return addedMatrix;
	}


	// Transposing a matrix
	public SparseMatrix transpose() {
		// Add your code here

		//Creating a new ArrayList and Matrix
		ArrayList<Entry> transposedEntries = new ArrayList<Entry>();
		SparseMatrix transposedMatrix = new SparseMatrix();
		int newNumCols = numRows;

		for (int i = 0; i < entries.size(); i++) { //For the size of the entries, 'swaps' the rows and columns...
			int val = entries.get(i).getValue();	//...and works out new value
			int currentPosition = entries.get(i).getPosition();
			int row = currentPosition/numCols;
			int col = currentPosition%numCols;

			int newPosition = row + newNumCols*col;

			Entry newElements = new Entry(newPosition, val);
			transposedEntries.add(newElements);
		}

		//Re-Merge the ArrayList and swaps the rows and columns
		transposedEntries = mergeSort(transposedEntries);
		transposedMatrix.numCols = numRows;
		transposedMatrix.numRows = numCols;
		transposedMatrix.entries = transposedEntries;

		return transposedMatrix;
	}

	// Matrix-vector multiplication
	public DenseVector multiply(DenseVector v) {
		// Add your code here
		DenseVector t = new DenseVector(numRows); //Create new DenseVector
		for (int i=0; i<entries.size(); i++) {  //For the number of entries
			//Find all the relevant values
			int currentPosition = entries.get(i).getPosition();
			int col = currentPosition%numCols;
			int row = (currentPosition - col)/numCols;
			int val = entries.get(i).getValue();

			int newValue = val*v.getElement(col); //Find the new value
			newValue += t.getElement(row); //Add the values from the same rows
			t.setElement(row, newValue); //Add the value to the new DenseVector
		}
		return t;
	}

	// Return the number of non-zeros
	public int numNonZeros() {
		// Add your code here
		return this.entries.size(); //Entries is a non-zero matrix, find the size
	}

	// Multiply the matrix by a scalar, and update the matrix elements
	public void multiplyBy(int scalar) {
		// Add your code here
		for (int i=0; i<entries.size(); i++) { //For the number of entries
			int newValue = entries.get(i).getValue() * scalar; //Find the new value by multiplying by scalar  
			entries.get(i).setValue(newValue); //Set the current value to the new value
		}
	}

	// Number of rows of the matrix
	public int getNumRows() {
		return this.numRows;
	}

	// Number of columns of the matrix
	public int getNumColumns() {
		return this.numCols;
	}

	// Output the elements of the matrix, including the zeros
	// Do not modify this method
	public void print() {
		int n_elem = numRows * numCols;
		int pos = 0;

		for (int i = 0; i < entries.size(); ++i) {
			int nonzero_pos = entries.get(i).getPosition();

			while (pos <= nonzero_pos) {
				if (pos < nonzero_pos) {
					System.out.print("0 ");
				} else {
					System.out.print(entries.get(i).getValue());
					System.out.print(" ");
				}

				if ((pos + 1) % this.numCols == 0) {
					System.out.println();
				}

				pos++;
			}
		}

		while (pos < n_elem) {
			System.out.print("0 ");
			if ((pos + 1) % this.numCols == 0) {
				System.out.println();
			}

			pos++;
		}
	}

	private int numRows; // Number of rows
	private int numCols; // Number of columns
	private ArrayList<Entry> entries; // Non-zero elements
}
