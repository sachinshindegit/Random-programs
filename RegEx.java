package com.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegEx {

	public static void main(String[] args) {
		Map dict = new HashMap();
	      Scanner in = new Scanner(System.in);
	      ArrayList names = new ArrayList();
	      int N=in.nextInt();
	      in.nextLine();
	      for(int i=0;i<N;i++)
	      {
	         String name=in.nextLine();
	         int phone=in.nextInt();
	         in.nextLine();
	          dict.put(name,phone);
	      }
	      while(in.hasNext())
	      {
	         String s=in.nextLine();
	         names.add(s);
	      }
	      for(int i=0;i<names.size();i++){
	    	  System.out.print(names.get(i)+"=");
	    	  if(dict.get(names.get(i)) != null){
	    		  System.out.print(dict.get(names.get(i)));
	    	  }else{
	    		  System.out.println("Not found");
	    	  }
	      }

	}

}

// tester.checker("(XXX|OOO|[.X|.O]\\3|X..X..X|O..O..O|([X|O]...)\\2X)$"); // Use \\ instead of using \ 