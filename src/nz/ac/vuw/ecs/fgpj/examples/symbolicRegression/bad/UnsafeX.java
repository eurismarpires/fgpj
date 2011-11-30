package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad;
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
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;

public class UnsafeX extends Terminal{

	private static double value;
	private static int KIND;
	
	public UnsafeX(){
		super(UnsafeReturnDouble.TYPENUM,"X");
	}
	
	@Override
	public UnsafeX getNew(GPConfig config) {
		return new UnsafeX();
	}

	@Override
	public void evaluate(ReturnData out) {
		UnsafeReturnDouble d = (UnsafeReturnDouble)out;
		d.setValue(UnsafeX.value);
	}

	@Override
	public Node copy(GPConfig conf) {
		return NodeFactory.newNode(getKind(), conf);
	}

	@Override
	public Node setKind(int kind) {
		KIND = kind;
		return this;
	}

	@Override
	public int getKind() {
		return KIND;
	}

	@Override
	public UnsafeX generate(String s, GPConfig conf) {
		if(s.startsWith(getName()))
			return (UnsafeX) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public UnsafeX generate(GPConfig conf) {
		return (UnsafeX) NodeFactory.newNode(getKind(), conf);
	}
	
	public static void setValue(double val){
		UnsafeX.value = val;
	}

}
