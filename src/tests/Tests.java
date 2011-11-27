package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import library.GPConfig;
import library.GeneticProgram;
import library.Node;
import library.Population;
import library.TournamentSelection;

import org.junit.Test;

import sets.lines.ImageFitness;
import sets.lines.Line;
import sets.lines.Null;
import sets.lines.ReturnColor;
import sets.lines.ReturnImage;
import sets.lines.SetColor;
import sets.symbolicRegression.Add;
import sets.symbolicRegression.RandomInt;
import sets.symbolicRegression.ReturnDouble;
import sets.symbolicRegression.SymbolicFitness;
import sets.symbolicRegression.Times;
import sets.symbolicRegression.X;

public class Tests {

	/**
	 * Current known problems: Mutation and crossover are probably keeping the tree full Grow program generation is
	 * actualy very full like, 
	 * 
	 * Mutation often does nothing
	 * 
	 * Program generation using those tables doesn't work for the full case as not all trees can be made that way
	 */

	@Test
	public void TestString1() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new SymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new SymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
		p.evolve(10);
		pop = p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
	}

	@Test
	public void TestDepthGeneration() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();

		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new Null(conf));
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
		for(int i =0 ;i < hist.length;i++){
			System.out.printf("%03d " , hist[i]);
		}		
		System.out.println();
	}
	
	@Test
	public void TestDepthMutation() {
		GPConfig conf = new GPConfig(1, 2, 8, 0.50, 0.4, 0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new SymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();

		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();

		int size = 10;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		conf.fitnessObject.initFitness();
		conf.selectionOperator = new TournamentSelection(5);

		int size = 200;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
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