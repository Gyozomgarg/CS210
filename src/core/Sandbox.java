package core;

import adt.CustomHashMap;
import adt.ListMap;
import java.util.Random;

public class Sandbox {
    public static void main(String[] args) {
        CustomHashMap<String,Object> temp = new CustomHashMap<String,Object>();
//	ListMap<String, Integer> temp = new ListMap<String,Integer>();
        int inserted = 0;
        int remove = 0;
        Random gen = new Random();

        for(int i = 0; i<26; i++){
            int random = gen.nextInt(47);
            if(temp.put(""+random, random*2) != null)
                inserted++;
        }
     
        System.out.println("Overrides (Same Key Value) via loop: "+inserted);
        System.out.println("Size of Map ="+temp.size());
        
        for(int i = 0; i<16; i++){
            int random = gen.nextInt(47);
            if(temp.remove(""+random) != null)
                remove++;
            
        }
        
        System.out.println("Removed via loop: "+remove);
        System.out.println("Size of Map ="+temp.size());
        
        System.out.println("Entry Set:");
        System.out.println(temp.entrySet());
        System.out.println("Key Set:");
        System.out.println(temp.keySet());
        System.out.println("Values:");
        System.out.println(temp.values());
        
        System.out.print("\n--->Create 2nd Map<---\n");
        
        CustomHashMap<String,Object> temp2 = new CustomHashMap<String,Object>();
        
        for(int i = 0; i<26; i++){
            int random = gen.nextInt(47);
            temp2.put(""+random, (char)('a'+i));
        }
        
        System.out.println("Size of 2nd Map ="+temp2.size());
        System.out.println("temp2 KeySet: ");
        System.out.println(temp2.keySet());
        System.out.println("temp2 EntrySet: ");
        System.out.println(temp2.entrySet());
        
        System.out.print("\n--->Put all Test<---\n");
        temp.putAll(temp2);
        
        System.out.println("Combined KeySet: ");
        System.out.println(temp.keySet());
        System.out.println("Combined EntrySet: ");
        System.out.println(temp.entrySet());
        
        System.out.println("Size of Combined Map ="+temp.size());
        
        
        
//        System.out.print("\n--->Double Put Test<---\n");
//        
//        temp.put(""+49, "oldvalue");
//        System.out.println("First put: " + temp.get(""+49));
//        temp.put(""+49, "9/11");
//        System.out.println("Second put: " + temp.get(""+49));
//        
//        System.out.print("\n--->Remove Test<---\n");
//        temp.remove(""+49);
//        System.out.println(temp.getTuple(""+49));
//        System.out.print("\n--->Does Value Still Exists<---\n");
//        System.out.println(temp.containsValue("9/11"));
//        
//        temp.put("yee", 911);
//        temp.put("Old Yeller", false);
//        temp.put("Skyrim with guns", "-IGN");
//        temp.put("Hi Aaron", null);      
//         
//        System.out.print("\n--->Make Sure the Tuples Are Actually There<---\n");
//        System.out.print(temp.getTuple("yee"));
//        System.out.print(temp.getTuple("Old Yeller"));
//        System.out.print(temp.getTuple("Skyrim with guns"));
//        System.out.print(temp.getTuple("Hi Aaron"));
//        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
//        System.out.println("Size = "+temp.size());
//        System.out.println(temp.entrySet());      
//        System.out.println(temp.keySet());
//        System.out.println(temp.values());
//        
//        System.out.print("\n--->Remove \"Old Yeller\" Test<---\n");
//        temp.remove("Old Yeller");
//        System.out.print(temp.getTuple("yee"));
//        System.out.print(temp.getTuple("Old Yeller"));
//        System.out.print(temp.getTuple("Skyrim with guns"));
//        System.out.print(temp.getTuple("Hi Aaron"));
//        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
//        System.out.println("Size = "+temp.size());
//        System.out.println(temp.entrySet());      
//        System.out.println(temp.keySet());
//        System.out.println(temp.values());
//        
//        
//        System.out.print("\n--->Remove \"yee\" Test<---\n");
//        temp.remove("yee");
//        System.out.print(temp.getTuple("yee"));
//        System.out.print(temp.getTuple("Old Yeller"));
//        System.out.print(temp.getTuple("Skyrim with guns"));
//        System.out.print(temp.getTuple("Hi Aaron"));
//        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
//        System.out.println("Size = "+temp.size());
//        System.out.println(temp.entrySet());      
//        System.out.println(temp.keySet());
//        System.out.println(temp.values());
//
//        System.out.print("\n--->Remove \"Skyrim with guns\" Test<---\n");
//        temp.remove("Skyrim with guns");
//        System.out.print(temp.getTuple("yee"));
//        System.out.print(temp.getTuple("Old Yeller"));
//        System.out.print(temp.getTuple("Skyrim with guns"));
//        System.out.print(temp.getTuple("Hi Aaron"));
//        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
//        System.out.println("Size = "+temp.size());
//        System.out.println("EntrySet: " + temp.entrySet());      
//        System.out.println("KeySet: " + temp.keySet());
//        System.out.println("Values: " + temp.values());     
//        
//        System.out.print("\n--->Remove \"Hi Aaron\" Test<---\n");
//        temp.remove("Hi Aaron");
//        System.out.print(temp.getTuple("yee"));
//        System.out.print(temp.getTuple("Old Yeller"));
//        System.out.print(temp.getTuple("Skyrim with guns"));
//        System.out.print(temp.getTuple("Hi Aaron"));
//        System.out.print("\n--->Entry Set, Key Set, Values<---\n");
//        System.out.println("Size = "+temp.size());
        System.out.print("\n--->All Done<---\n");
    }
}
