package nz.ac.vuw.ecs.fgpj.core;

/*
 FGPJ Genetic Programming library
 Copyright (C) 2011  Roman Klapaukh

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.List;

/**
 * The Terminal class represents a terminal is a GP program tree
 * 
 * @author Roman Klapaukh
 * 
 */
public abstract class Terminal extends Node {

	/**
	 * Make a new terminal. It has a return type and a name. The name cannot
	 * have any whitespace in it
	 * 
	 * Another important restriction is that you cannot have two Nodes such that
	 * the name of one is the prefix of another, e.g., Int and Integer. This is
	 * because of the way that nodes are identified when being parsed from
	 * files. While this problem could be avoided it would require more code on
	 * the user end which does not seem worth while.
	 * 
	 * @param type
	 *            The return type
	 * @param name
	 *            name of the terminal (with no whitespace)
	 */
	public Terminal(int type, String name) {
		super(type, 0, name);
	}

	public int computeSize() {
		return 1;
	}

	public int traceDepth(int curDepth) {
		return 1;
	}

	public int computeDepth(int curDepth) {
		setDepth(curDepth + 1);
		return 1;
	}

	public void addTreeToVector(List<Node> list) {
		list.add(this);
	}

	public void addTreeToVector(List<Node> list, int typeNum) {
		if (getReturnType() == typeNum)
			list.add(this);
	}

	public void print(StringBuilder s) {
		s.append(getName());
	}

	public int computePositions(int parent) {
		this.setPosition(parent + 1);
		return parent + 1;
	}

	public Node getNode(int node) {
		if (this.getPosition() == node) {
			return this;
		}
		return null;
	}

	public Node getNode(int node, int type, Node best) {
		if (this.getReturnType() == type
				&& (best == null || this.getPosition() == node || Math.abs(best
						.getPosition() - node) > Math.abs(this.getPosition()
						- node))) {
			return this;
		}
		return best;
	}

	@SuppressWarnings("unchecked")
	public Terminal generate(String s, GPConfig conf) {
		return (Terminal) this.getNew(conf);
	}

	@SuppressWarnings("unchecked")
	public Terminal generate(GPConfig conf) {
		return  (Terminal) this.getNew(conf);
	}

	@Override
	public final Node copy(GPConfig conf) {
		Node n =  this.getNew(conf);
		n.init(this);
		return n;
	}
	
	@Override
	public void delete() {
		this.setParent(null);
		this.setDepth(0);
	}
}
