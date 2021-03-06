package nz.ac.vuw.ecs.fgpj.test;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;
import nz.ac.vuw.ecs.fgpj.core.TournamentSelection;
import nz.ac.vuw.ecs.fgpj.examples.lines.Line;
import nz.ac.vuw.ecs.fgpj.examples.lines.Null;
import nz.ac.vuw.ecs.fgpj.examples.lines.ReturnColor;
import nz.ac.vuw.ecs.fgpj.examples.lines.ReturnImage;
import nz.ac.vuw.ecs.fgpj.examples.lines.SetColor;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Add;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.RandomInt;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Times;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad.UnsafeReturnDouble;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad.UnsafeSymbolicFitness;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad.UnsafeX;

import org.junit.Test;


public class Tests {

	@Test
	public void TestString1() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addTerminal(new RandomInt(0,10,conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new UnsafeSymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			StringBuilder sso = new StringBuilder();
			StringBuilder ssc = new StringBuilder();
			pr.print(sso);
			pr.parseProgram(sso.toString(), conf);
			pr.print(ssc);
			assertTrue(sso.toString().equals(ssc.toString()));
		}
	}

	@Test
	public void TestString2() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addTerminal(new RandomInt(0,10,conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new UnsafeSymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			StringBuilder ssc = new StringBuilder();
			String myString = "1 Program0  ( * X X )  |";
			pr.parseProgram(myString, conf);
			pr.print(ssc);
			assertTrue("\"" + ssc.toString().trim() + "\"" + "\"" + myString + "\"",
					ssc.toString().trim().equals(myString));
			assertTrue(pr.getDepth(0) == 2);
		}
	}

	@Test
	public void TestSize() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
		p.evolve(10);
		pop = p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
	}

	
	@Test
	public void TestParralelFitness() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new ParallelFitness<UnsafeSymbolicFitness>(new UnsafeSymbolicFitness(),4,7);
		conf.selectionOperator = new TournamentSelection(5);

		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		p.evolve(1);
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pp: pop){
			assertFalse("Fitness is not being assigned",Double.isNaN(pp.getFitness()));
		}
	}
	
	@Test
	public void TestDepthGeneration() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			assertTrue("depth: " + pr.getDepth(0),
					pr.getDepth(0) >= conf.minDepth() && pr.getDepth(0) <= conf.maxDepth());
		}
	}

	@Test
	public void TestChangeGeneration() {
		GPConfig conf = new GPConfig(1, 1, 10, 0.50, 0.5, 0.0);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop1 = p.getUnderlyingPopulation();
		
		String[] gen0  = new String[pop1.size()];
		for (int i = 0 ;i < pop1.size();i++) {
			StringBuilder ss = new StringBuilder();
			pop1.get(i).print(ss);
			gen0[i] = ss.toString();
		}
		
		p.evolve(1);
		pop1 = p.getUnderlyingPopulation();

		String[] gen1  = new String[pop1.size()];
		for (int i = 0 ;i < pop1.size();i++) {
			StringBuilder ss = new StringBuilder();
			pop1.get(i).print(ss);
			gen1[i] = ss.toString();
		}
		
		int countDiff = 0;
		for (int i = 0 ; i < gen0.length; i++ ) {
			boolean matched = false;
			for (int j = i+1 ; j <gen1.length ; j++ ) {
				if (gen0[i].equals(gen1[j])) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				countDiff++;
			}
		}
//		System.out.println(countDiff);
		assertTrue(""+countDiff,countDiff != 0);
	}

	@Test
	public void TestRandom() {
		GPConfig conf = new GPConfig(1, 3, 8, 0.50, 0.4, 0.1);
		conf.addFunction(new Line(conf));
		conf.addTerminal(new Null());
		conf.addTerminal(new SetColor(conf));

		int size = 100;
		
		Population p = new Population(size, conf);
		p.setReturnType(ReturnImage.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		GeneticProgram pr = pop.get(0);
		int [] hist = new int[pr.getSize(0)];
		for(int i= 0 ; i< pr.getSize(0)*100;i++){
			hist[pr.getRandomNode(ReturnColor.TYPENUM,0, conf).getPosition()] ++;
		}
		//Can this be automated?
//		for(int i =0 ;i < hist.length;i++){
//			System.out.printf("%03d " , hist[i]);
//		}		
//		System.out.println();
	}
	
	@Test
	public void TestDepthMutation() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			conf.mutationOperator.mutate(pr, conf);
			assertTrue("depth: " + pr.getDepth(0), pr.getDepth(0) <= conf.maxDepth());
		}
	}
	
	@Test
	public void TestChangeMutation() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		int count =0;
		for (GeneticProgram pr : pop) {
			StringBuilder ss = new StringBuilder();
			pr.print(ss);
			String orig = ss.toString();
			conf.mutationOperator.mutate(pr, conf);
			ss = new StringBuilder();
			pr.print(ss);
			String news = ss.toString();
//			if(orig.equals(news)){
//				System.out.println(news);
//				count ++;
//			}
		}
		assertTrue(""+count, count < .01 * size);
	}

	@Test
	public void TestDepthCrossover() {
		GPConfig conf = new GPConfig(1, 1, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			conf.crossoverOperator.crossover(pr, pr, 100, conf);
			assertTrue("depth: " + pr.getDepth(0), pr.getDepth(0) <= conf.maxDepth());
		}
	}

	@Test
	public void TestMemory() {
		GPConfig conf = new GPConfig(1, 2, 3, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();

		int size = 10;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		p.evolve(1000);

		List<GeneticProgram> pop = p.getUnderlyingPopulation();

		for (GeneticProgram pr : pop) {
			ProgramIter ii = new ProgramIter(pr, conf);
			while (ii.hasNext()) {
				int clash = 0;
				Node n = ii.next();
				for (GeneticProgram pr2 : pop) {
					ProgramIter i = new ProgramIter(pr2, conf);
					while (i.hasNext()) {
						if (n == i.next()) {
							clash++;
						}
					}
				}
				assertTrue(clash == 1);
			}
		}
	}

	
	@Test
	public void TestNotWorseGeneration() {
		GPConfig conf = new GPConfig(1, 1, 4, 0.50, 0.4, 0.1);
		conf.addTerminal(new UnsafeX());
		conf.addFunction(new Times());
		conf.fitnessObject = new UnsafeSymbolicFitness();
		conf.fitnessObject.initFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 200;
		Population p = new Population(size, conf);
		p.setReturnType(UnsafeReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		conf.fitnessObject.assignFitness(p.getUnderlyingPopulation(), conf);
		Collections.sort(p.getUnderlyingPopulation(),conf.fitnessObject);
		GeneticProgram  fitness =p.getUnderlyingPopulation().get(p.getNumIndividuals()-1).copy(conf);
		
		p.nextGeneration();
		
		int count = 100;
		for(int i =0;i<count;i++){
			conf.fitnessObject.assignFitness(p.getUnderlyingPopulation(), conf);
			Collections.sort(p.getUnderlyingPopulation(),conf.fitnessObject);
			GeneticProgram fitness2 =p.getUnderlyingPopulation().get(p.getNumIndividuals()-1).copy(conf);
			assertTrue("Programs get worse", conf.fitnessObject.compare(fitness, fitness2)<=0);
			fitness = fitness2;
		}
		
	}
	
	private static class ProgramIter implements Iterator<Node> {

		private Iterator<Node> nodes;

		public ProgramIter(GeneticProgram p, GPConfig conf) {
			List<Node> l = new ArrayList<Node>();
			for (int i = 0; i < conf.getNumRoots(); i++) {
				p.getRoot(i).addTreeToVector(l);
			}
			nodes = l.iterator();
		}

		@Override
		public boolean hasNext() {
			return nodes.hasNext();
		}

		@Override
		public Node next() {
			return nodes.next();
		}

		@Override
		public void remove() {
			nodes.remove();
		}

	}
}
