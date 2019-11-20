import java.util.Scanner;

public class Mapa {
	
	//Aqui, são exemplos simples para trabalhar com matriz
	//Caso queiram explorar um pouco mais: http://introcs.cs.princeton.edu/java/22library/Matrix.java.html
	
	public void montaMapaSimples(){	
		double m[][] = {
		      { 0*0, 1*0, 2*0, 3*0 },
		      { 0*1, 1*1, 2*1, 3*1 },
		      { 0*2, 1*2, 2*2, 3*2 },
		      { 0*3, 1*3, 2*3, 3*3 }
		    };
		    int i, j;
		   
		    for(i=0; i<4; i++) {
		      for(j=0; j<4; j++)
		        System.out.print(m[i][j] + " ");
		      System.out.println();
		    }
	}
	
	//Brincando com matrizes : )
	public void montaMapaPassandoLinhasEColunas(){
		int m, n, c, d;
      	Scanner in = new Scanner(System.in);
 
      	System.out.println("Entre com o nro de linhas e colunas da matriz desejada:");
      	m = in.nextInt();
      	n  = in.nextInt();
 
      	int first[][] = new int[m][n];
      	int second[][] = new int[m][n];
      	int sum[][] = new int[m][n];
 
      	System.out.println("Entre com os elementos da primeira matriz:");
 
      	for (  c = 0 ; c < m ; c++ )
      		for ( d = 0 ; d < n ; d++ )
      			first[c][d] = in.nextInt();
 
      	System.out.println("Entre com os elementos da segunda matriz:");
 
      	for ( c = 0 ; c < m ; c++ )
      		for ( d = 0 ; d < n ; d++ )
      			second[c][d] = in.nextInt();
 
      	for ( c = 0 ; c < m ; c++ )
      		for ( d = 0 ; d < n ; d++ )
      			sum[c][d] = first[c][d] + second[c][d];  //alterem '+' por '-' para subtração entre matrizes
 
      	System.out.println("Soma as matrizes de entrada:-");
 
      	for ( c = 0 ; c < m ; c++ )
      	{
      		for ( d = 0 ; d < n ; d++ )
      			System.out.print(sum[c][d]+"\t");
 
      		System.out.println();
      	}
   	}
}