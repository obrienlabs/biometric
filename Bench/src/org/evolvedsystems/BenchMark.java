/*
 * 
 * 20110127: remove ArrayList creation GC time, we now run 58% faster 17.2billion/day
 * old P,0,15733191, 15733191,704 159424614880 ,T,169753
 * new P,0,15733191,15733191,704	,159424614880	,94808	,
 */

package org.evolvedsystems;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BenchMark {
	public static final BigInteger TWO = BigInteger.valueOf(2);
	public static final BigInteger THREE = BigInteger.valueOf(3);
	public static final BigInteger FOUR = BigInteger.valueOf(4);
	public static final BigInteger PATH_OFFSET_FOUR = BigInteger.valueOf(2);
	
	private BigInteger sequenceMax;
	private BigInteger sweepMax;
	private int sequencePath;
	private int sweepPath;
	

	// Constants
	public static final long R2000 = 67457283406188652L;
	public long pathCache[] = {0,1,2,7,3};

	//private BigInteger iterations = BigInteger.valueOf(0);
	
	public BigInteger factorial(BigInteger number) {
		if(number.compareTo(BigInteger.ONE) < 1) {
			return BigInteger.ONE;
		} else {
			return factorial(number.subtract(BigInteger.ONE)).multiply(number);
		}
	}
	
/*	public List<Boolean> allocate(long levels) {
		List<Boolean> aList = new ArrayList<Boolean>();
		for(long i=0;i<levels;i++) {
			aList.add(new Boolean(false));
		}
		return aList;
	}*/
	
	public void hailstoneSequence(BigInteger start)  {
		sequenceMax = BigInteger.ONE;
		sequencePath = 2;//PATH_OFFSET_FOUR;
		//list.remove(0);
		//list.remove(0);		
		//List<BigInteger> sequence = new ArrayList<BigInteger>();
		//sequence.add(start);
		if(start.equals(BigInteger.ZERO) || start.equals(BigInteger.ONE)) {
			//list.add(max);
			//list.add(Integer.valueOf(path));
			return;//list;//sequence;
		}
		BigInteger current = start;		
		while (current.compareTo(FOUR) > 0) { // Perf:1%: 6672 to 6609
			if(current.testBit(0)) { // test odd
				current = current.shiftLeft(1).add(current).add(BigInteger.ONE);
			} else {
				current = current.shiftRight(1);//.divideAndRemainder(TWO)[0];				
			}
			// check max
			if(sequenceMax.compareTo(current) < 0) {
				sequenceMax = current;
			}
			sequencePath = sequencePath + 1;//.add(BigInteger.ONE);
			//sequence.add(current);
		}
		//list.add(max);
		//list.add(Integer.valueOf(path));
		return;// list;//sequence;		
	}

	public List<BigInteger> hailstoneSequence(BigInteger current, List<BigInteger> list)  {
		return list;
	}

	public BigInteger getSequenceMax() {
		return sequenceMax;
	}

	public void setSequenceMax(BigInteger sequenceMax) {
		this.sequenceMax = sequenceMax;
	}

	public BigInteger getSweepMax() {
		return sweepMax;
	}

	public void setSweepMax(BigInteger sweepMax) {
		this.sweepMax = sweepMax;
	}

	public int getSequencePath() {
		return sequencePath;
	}

	public void setSequencePath(int sequencePath) {
		this.sequencePath = sequencePath;
	}

	public int getSweepPath() {
		return sweepPath;
	}

	public void setSweepPath(int sweepPath) {
		this.sweepPath = sweepPath;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// get parameters
		int sector = 32768;//Integer.parseInt(args[0]);
		int mult = 16;//Integer.parseInt(args[1]);
		BenchMark aBench = new BenchMark();
		StringBuffer buffer = null;
		long x = 0;
		//long lastVal = Long.MAX_VALUE;
		long lastVal = 4294967296L;
		Calendar calendar = GregorianCalendar.getInstance();
		long startTime = calendar.getTimeInMillis();
		long endTime;
/*		for(int y=0;y<1;y++) {
			//startTime = GregorianCalendar.getInstance().getTimeInMillis();
			for(x=0;x< lastVal;x++) {
				x = x + 1 - 1;
//				buffer = new StringBuffer("N: ");
//				buffer.append(x);
//				buffer.append(" L: ");
//				buffer.append(aBench.factorial(BigInteger.valueOf(x)).toString());
				//System.out.println(buffer.toString());
			}
			endTime = calendar.getTimeInMillis() - startTime;
			System.out.println(y + ":" + x + "," + endTime + "," + (x/((1 + endTime)/1000)) + " it/sec");
		}*/

		int maxPath = 0;//, path should fit in 64 bits
		int maxPathIteration = 0;//, path should fit in 64 bits
		BigInteger maxValue = BigInteger.ONE;
		//BigInteger currentMax = BigInteger.ONE;
		BigInteger maxValueIteration = BigInteger.ONE;
		//BigInteger tempMax = BigInteger.ONE;
		boolean milestone = false;
		String prefix = null;
		long rangeStart = (sector * mult) * 1048576L;//67108864L;//4294967296L;
		long rangeEnd = rangeStart + (mult * 1048576L);//67108864L);//4294967296L);
		long rangeInterval = (mult * 65536);//1048576;//67108864L;//134217728L;//268435456L;
		System.out.println("Proc cores:  " + Runtime.getRuntime().availableProcessors());
		//System.out.println("Runtime    : " + Runtime.getRuntime());
		System.out.println("Short max:   " + Short.MAX_VALUE);
		System.out.println("Integer max: " + Integer.MAX_VALUE);
		System.out.println("Long max:    " + Long.MAX_VALUE);		
		System.out.println("Partition:   " + rangeInterval);
		System.out.println("Range:       " + rangeStart + " to: " + rangeEnd);
		Calendar aCalendar = GregorianCalendar.getInstance();
		
/*		BigInteger aPowerOf2 = BigInteger.ONE;
		for(int i=1;i<1025;i++) {
			aPowerOf2 = aPowerOf2.shiftLeft(1);
			//assertEquals(i,aPowerOf2.bitLength());
			System.out.println("2^" + (aPowerOf2.bitLength() - 1) + "\t~ 10^" + aPowerOf2.toString().length() + " \t= " + aPowerOf2);
		}*/
		long timeStartMs = aCalendar.getTimeInMillis();
		long timeStartNs = System.nanoTime();
		BigInteger bigNumber = BigInteger.ONE;
		for(long i=0;i<2;i++) {
			bigNumber = bigNumber.shiftLeft(1024);
		}
		System.out.println("Googolplex = " + bigNumber.bitLength() + "," + bigNumber.toString().length() + "," + bigNumber);
		long timeEndMs = System.currentTimeMillis();
		long timeEndNs = System.nanoTime();		
		System.out.println("Duration: " + (timeEndMs - timeStartMs) + " ms");
		System.out.println("Duration: " + (timeEndNs - timeStartNs) + " ns (+- 15 ns)");// - (primarily due to screen display overhead - even though the console is hidden");
		
		//List list = new ArrayList();
		//list.add(TWO); // placeholder for max
		//list.add(TWO); // placeholder for path
		startTime = calendar.getTimeInMillis();
/*		List customSequence = aBench.hailstoneSequence(list, maxValue, maxPathIteration,BigInteger.valueOf(BenchMark.R2000));
		endTime = calendar.getTimeInMillis();
		System.out.println("R2000: " + BenchMark.R2000 + " = P:" + maxPathIteration + 
				" M:" + maxValue + " t(ms): " + (endTime - startTime));
*/		long currentNumber = rangeStart;
		
		/**
		 * We use a double loop to show interim progress by splitting up the search space into quadrants
		 */
		for(long interval = 0;interval < 16;interval++) { // sectors are dived by two (we only test odd numbers)
			currentNumber = currentNumber + 1;
			for(long index=1;index<rangeInterval;index+=2) {// powers of two (starting at 0 here) are never record holders 		
				aBench.hailstoneSequence(BigInteger.valueOf(currentNumber));
				//if(!list.isEmpty()) {
					//currentMax = maxValueIteration;
					maxValueIteration = aBench.getSequenceMax();//.(BigInteger)list.remove(0);
					maxPathIteration = aBench.getSequencePath();//(Integer)list.remove(0);
					if(maxPathIteration > maxPath) {
						milestone = true;
						maxPath = maxPathIteration;
						prefix = "P";
					}
				
					if(maxValueIteration.compareTo(maxValue) > 0) {
						if(milestone) {
							prefix = "PM";
						} else {
							milestone = true;
							prefix = "M";
						}
						maxValue = maxValueIteration;
					}
					if(milestone) {
						buffer = new StringBuffer(prefix);
						buffer.append(",");//N,");
						buffer.append(interval);
						buffer.append(",");
						buffer.append(index);
						buffer.append(",");				
						buffer.append(BigInteger.valueOf(currentNumber).bitLength());
						buffer.append(",");				
						buffer.append(Long.valueOf(currentNumber).toString().length());
						buffer.append(",");				
						buffer.append(currentNumber);
						buffer.append(",");//L,");			
						buffer.append(maxPath);// + PATH_OFFSET_FOUR); // we stop the search at 4 (so add a path of 2)			
						buffer.append("\t");
						buffer.append(",");
						buffer.append(maxValueIteration.bitLength()); 
						buffer.append(",");
						buffer.append(maxValueIteration.toString().length()); 
						buffer.append(",");//M,");
						buffer.append(maxValueIteration); // BigInteger implements Comparable
						//buffer.append(list.toString());
						buffer.append("\t");
						buffer.append(",");//T,");
						buffer.append(GregorianCalendar.getInstance().getTimeInMillis() - startTime);
						buffer.append("\t");
						if((maxValueIteration.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)) {
							buffer.append("2^63+,");
							buffer.append(maxValueIteration.subtract(BigInteger.valueOf(Long.MAX_VALUE)));
							//buffer.append(",");
						} else {
							buffer.append(",");
						}
				
						//buffer.append(",F,");
						//buffer.append(Runtime.getRuntime().freeMemory());				
						System.out.println(buffer.toString());
						milestone = false;
					}
				//}
				// increment
				currentNumber += 2;
			}
		}

		//aBench.allocate(3000000); // 4 heap space
//		aBench.
	}
}
