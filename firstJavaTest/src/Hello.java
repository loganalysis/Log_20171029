

class Hello{
	public static void main(String[] args) {
		 System.out.println( "DD!" );
		 String tem = new String("I  am a boy!");
		 String[] split = tem.split("[ ]+");
		 for(int i = 0 ; i!=split.length; ++i)
			 System.out.println(split[i]);
	}
}
