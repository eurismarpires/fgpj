package library;

import java.util.Vector;

public abstract class Function extends Node {

	private Node args[];

	private int argReturnTypes[];

	/**
	 * Constuctor
	 * 
	 */
	public Function(int type, int numArgs, String n, GPConfig conf) {
		super(type, numArgs, n, conf);
		args = null;
		argReturnTypes = null;
		// If the number of arguments this function accepts is greater than zero
		// then we need to allocate the space to store pointers to the arguments
		// and to store their return types.
		if (numArgs > 0) {
			args = new Node[numArgs];
			argReturnTypes = new int[numArgs];

			for (int i = 0; i < numArgs; i++) {
				args[i] = null;
				argReturnTypes[i] = -1;
			}
		}
	}

	/**
	 * Set the Nth argument
	 */
	public void setArgN(int N, Node n) {
		if (N >= maxArgs || N < 0)
			throw new IllegalArgumentException("Invalid argument number");

		if (n == null)
			throw new IllegalArgumentException("Node is NULL");

		if (n.getReturnType() != argReturnTypes[N])
			throw new IllegalArgumentException(
					"Incorrect return type for argument " + N);

		args[N] = n;
		n.setParent(this);
	}

	/**
	 * Get the Nth argument
	 */
	public Node getArgN(int N) {
		if (N >= maxArgs || N < 0)
			throw new IllegalArgumentException("Invalid argument number");

		return args[N];
	}

	/** Set the return type of the Nth argument */
	public void setArgNReturnType(int N, int type) {
		if (N >= maxArgs || N < 0)
			throw new IllegalArgumentException(
					"Function::setArgNReturnType, Invalid argument number");

		if (type <= 0)
			throw new IllegalArgumentException(
					"Function::setArgNReturnType, Invalid return type");

		argReturnTypes[N] = type;
	}

	/** Get the return type of the Nth argument */
	public int getArgNReturnType(int N) {
		if (N >= maxArgs || N < 0)
			throw new IllegalArgumentException(
					"Function::getArgNReturnType, Invalid argument number");

		return argReturnTypes[N];
	}

	/**
	 * Recursively compute the size of the subtree rooted at this function by
	 * calling computeSize for each argument
	 */
	public int computeSize() {
		int size = 0;

		for (int i = 0; i < maxArgs; i++) {
			size += args[i].computeSize();
		}

		return size + 1;
	}

	/**
	 * Recursively compute the depth of the subtree rooted at this function by
	 * calling computeDepth for each argument.
	 */
	public int computeDepth(int curDepth) {
		setDepth(curDepth + 1);

		int retDepth = 0;
		int maxDepth = 0;

		for (int i = 0; i < maxArgs; i++) {
			if ((retDepth = args[i].computeDepth(getDepth())) > maxDepth)
				maxDepth = retDepth;
		}

		return maxDepth + 1;
	}

	/** Adds the current node to the vector */
	public void addToVector(Vector<Node> vec) {
		vec.add(this);

		for (int i = 0; i < maxArgs; i++) {
			args[i].addToVector(vec);
		}
	}

	/**
	 * Adds the current node to the vector iff this function returns the type
	 * typeNum
	 */
	public void addToVector(Vector<Node> vec, int typeNum) {
		if (getReturnType() == typeNum)
			vec.add(this);

		for (int i = 0; i < maxArgs; i++) {
			args[i].addToVector(vec, typeNum);
		}
	}

	/** Print this function to the string s */
	public void print(StringBuffer s) {
		s.append(" ( ");
		s.append(getName());

		for (int i = 0; i < maxArgs; i++) {
			s.append(" ");
			args[i].print(s);
		}

		s.append(" ) ");
	}

	public void unhook(int i) {
		if (i >= maxArgs || i < 0)
			throw new IllegalArgumentException("Invalid argument number");

		args[i] = null;
	}

	public void unhook(){
		   for (int i =0; i< maxArgs;i++){
			   args[i] = null;
		   }
	}
}