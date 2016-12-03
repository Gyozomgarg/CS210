package core;

import adt.CustomHashMap;
import adt.ListMap;
import java.util.Random;

public class Sandbox {
    public static void main(String[] args) {
        CustomHashMap<String,Object> temp = new CustomHashMap<String,Object>();
//	ListMap<String, Integer> temp = new ListMap<String,Integer>();
        int inserted = 0;
        Random gen = new Random();

//        for(int i = 0; i<15; i++){
//            int random = gen.nextInt(50000);
//            temp.put(""+random, random);
//            inserted++;
//        }
        
        System.out.println("Inserted via loop: "+inserted);
        
        System.out.print("\n--->Double Put Test<---\n");
        
        temp.put(""+49, "oldvalue");
        System.out.println("First put: " + temp.get(""+49));
        temp.put(""+49, "9/11");
        System.out.println("Second put: " + temp.get(""+49));
        
        System.out.print("\n--->Remove Test<---\n");
        temp.remove(""+49);
        System.out.println(temp.getTuple(""+49));
        System.out.print("\n--->Does Value Still Exists<---\n");
        System.out.println(temp.containsValue("9/11"));
        
//        for(int i = 0; i<10; i++){
//            int random = gen.nextInt(500000);
//            temp.remove(""+random);
//        }
        
        
        temp.put("yee", 911);
        temp.put("Old Yeller", false);
        temp.put("Skyrim with guns", "-IGN");
        temp.put("Hi Aaron", null);      
         
        System.out.print("\n--->Make Sure the Tuples Are Actually There<---\n");
        System.out.print(temp.getTuple("yee"));
        System.out.print(temp.getTuple("Old Yeller"));
        System.out.print(temp.getTuple("Skyrim with guns"));
        System.out.print(temp.getTuple("Hi Aaron"));
        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
        System.out.println("Size = "+temp.size());
        System.out.println(temp.entrySet());      
        System.out.println(temp.keySet());
        System.out.println(temp.values());
        
        System.out.print("\n--->Remove \"Old Yeller\" Test<---\n");
        temp.remove("Old Yeller");
        System.out.print(temp.getTuple("yee"));
        System.out.print(temp.getTuple("Old Yeller"));
        System.out.print(temp.getTuple("Skyrim with guns"));
        System.out.print(temp.getTuple("Hi Aaron"));
        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
        System.out.println("Size = "+temp.size());
        System.out.println(temp.entrySet());      
        System.out.println(temp.keySet());
        System.out.println(temp.values());
        
        
        System.out.print("\n--->Remove \"yee\" Test<---\n");
        temp.remove("yee");
        System.out.print(temp.getTuple("yee"));
        System.out.print(temp.getTuple("Old Yeller"));
        System.out.print(temp.getTuple("Skyrim with guns"));
        System.out.print(temp.getTuple("Hi Aaron"));
        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
        System.out.println("Size = "+temp.size());
        System.out.println(temp.entrySet());      
        System.out.println(temp.keySet());
        System.out.println(temp.values());

        System.out.print("\n--->Remove \"Skyrim with guns\" Test<---\n");
        temp.remove("Skyrim with guns");
        System.out.print(temp.getTuple("yee"));
        System.out.print(temp.getTuple("Old Yeller"));
        System.out.print(temp.getTuple("Skyrim with guns"));
        System.out.print(temp.getTuple("Hi Aaron"));
        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
        System.out.println("Size = "+temp.size());
        System.out.println("EntrySet: " + temp.entrySet());      
        System.out.println("KeySet: " + temp.keySet());
        System.out.println("Values: " + temp.values());     
        
        System.out.print("\n--->Remove \"Hi Aaron\" Test<---\n");
        temp.remove("Hi Aaron");
        System.out.print(temp.getTuple("yee"));
        System.out.print(temp.getTuple("Old Yeller"));
        System.out.print(temp.getTuple("Skyrim with guns"));
        System.out.print(temp.getTuple("Hi Aaron"));
        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
        System.out.println("Size = "+temp.size());
        System.out.print("\n--->All Done<---\n");
    }
}
