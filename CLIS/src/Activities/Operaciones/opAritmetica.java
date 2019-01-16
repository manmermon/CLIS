/*
 * Copyright 2011-2013 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */ 

package Activities.Operaciones;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class opAritmetica
{
	private int resultado = 0;
	private String oper = "1 - 1 = ?";
	private int dificultad = 0;

	private final char[] operadores = { '+', '-', '*', '/' };

	private final int operSumRes = 0;
	private final int operMultDiv = 1;
	private final int operSumResMult = 2;
	private final int operTodos = 3;

	public opAritmetica(int dif)
	{
		if (dif <= 0)
		{
			this.dificultad = 0;
		}
		else if (dif >= 4)
		{
			this.dificultad = 4;
		}
		else
		{
			this.dificultad = dif;
		}
	}

	public void setDificultad(int dif)
	{
		if (dif <= 0)
		{
			this.dificultad = 0;
		}
		else if (dif >= 4)
		{
			this.dificultad = 4;
		}
		else
		{
			this.dificultad = dif;
		}
	}

	public int getDificultad()
	{
		return this.dificultad;
	}

	public int getResultado()
	{
		return this.resultado;
	}

	public String getOperacion()
	{
		return this.oper;
	}

	public void generarOperacion()
	{
		this.operacion();				
		//System.out.println("opAritmetica.generarOperacion(), dif = " + dificultad + " ---> "+this.oper + " res= " + this.resultado);		
	}
	private char genearOperador(int conjunto)
	{
		//conjunto = 0 -> +-, conjunto = 1 -> +/, conjunto = 2 -> +-*, conjunto >= 3 -> +-*/	  
		int op = (int)(Math.random() * 10.0D) % 2;

		if (conjunto == this.operMultDiv )
		{
			op += 2;
		}
		else if (conjunto == this.operTodos )
		{
			op = (int)(Math.random() * 10.0D) % 4;
		}
		else if (conjunto == this.operSumResMult )
		{
			op = (int)(Math.random() * 10.0D) % 3;
		}	

		return this.operadores[op];
	}

	private List<Integer> descomposicionFactoresPrimos(int numero)
	{
		List< Integer > df = new LinkedList< Integer >();

		if (numero < 0)
		{
			df.add( -1 );
		}
		else
		{
			df.add( 1 );
		}

		df.addAll(descomposicionFactoresPrimos_Aux(numero));

		return df;
	}

	private List<Integer> descomposicionFactoresPrimos_Aux(int numero)
	{
		List<Integer> df = new LinkedList< Integer >();

		int n = Math.abs(numero);

		if (n < 4)
		{
			df.add( n );
		}
		else
		{
			boolean enc = false;

			for (int i = 2; i <= Math.floor(Math.sqrt(n)); i++)
			{
				if (n % i == 0)
				{
					enc = true;

					df.add( i );
					df.addAll(descomposicionFactoresPrimos_Aux(n / i));
					break;
				}
			}

			if ( !enc )
			{
				df.add( n );
			}
		}

		return df;
	}

	private void operacion()
	{
		this.resultado = ((int)(Math.random() * 10.0D));

		this.oper = "";

		if (this.dificultad == 0)
		{
			char op = genearOperador( this.operSumRes );
			int min = 0;int max = 9;

			int[] n = getNumeros(this.resultado, op, min, max);

			this.oper = (n[0] + " " + op + " " + n[1] + " = ?");
		}
		else if (this.dificultad == 1)
		{
			int min = 0;int max = 9;
			int tipoArbol = (int)(Math.random() * 10.0D) % 2;

			if (tipoArbol == 0) // A op ( B op C )
			{
				char op1 = genearOperador(0);
				int[] n1 = getNumeros(this.resultado, op1, min, max);

				char op2 = genearOperador( this.operSumRes );
				if (n1[1] < 0)
				{
					op2 = '-';
				}
				int[] n2 = getNumeros(n1[1], op2, 0, max);

				this.oper = (n1[0] + " " + op1 + " ");

				if (op1 == '-')
				{
					this.oper += "( ";
				}

				this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");

				if (op1 == '-')
				{
					this.oper += ") ";
				}

				this.oper += "= ?";
			}
			else // A op B op C  = (A op B) op C
			{
				char op2 = genearOperador(0);
				int[] n2 = getNumeros(this.resultado, op2, min, max);

				char op1 = genearOperador( this.operSumRes );
				if (n2[0] < 0)
				{
					op1 = '-';
				}

				int[] n1 = getNumeros(n2[0], op1, 0, max);

				this.oper = (n1[0] + " " + op1 + " " + n1[1] + " " + op2 + " " + n2[1] + " = ?");
			}
		}
		else if (this.dificultad == 2)
		{
			int min = 0;int max = 99;
			int numNumeros = 2 + (int)(Math.random() * 10.0D) % 2;
			numNumeros = 3;
			if (numNumeros == 2)
			{
				char op = genearOperador(2);
				int[] n = getNumeros(this.resultado, op, min, max);

				this.oper = (n[0] + " " + op + " " + n[1] + " = ?");
			}
			else
			{
				int tipoArbol = (int)(Math.random() * 10.0D) % 2;

				if (tipoArbol == 0) // A op ( B op C )
				{
					char op1 = genearOperador( this.operSumResMult );
					int[] n1 = getNumeros(this.resultado, op1, min, max);

					char op2 = genearOperador(0);
					int[] n2 = getNumeros(n1[1], op2, min, max);

					if (n1[1] > 9)
					{
						if ((op2 == '+') && (n1[1] > 19))
						{
							if (Math.random() > 0.5D)
							{
								n2[1] = ((int)(Math.random() * 10.0D) % 10);
								n2[0] = (n1[1] - n2[1]);
							}
							else
							{
								n2[0] = ((int)(Math.random() * 10.0D) % 10);
								n1[1] -= n2[0];
							}
						}
						else if (op2 == '-')
						{
							n2[1] = ((int)(Math.random() * 10.0D) % 10);
							n2[0] = (n2[1] + n1[1]);
						}
					}

					this.oper = (n1[0] + " " + op1 + " ");

					if ((op1 == '-') || (op1 == '*'))
					{
						this.oper += "( ";
					}

					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");

					if ((op1 == '-') || (op1 == '*'))
					{
						this.oper += ") ";
					}

					this.oper += "= ?";
				}
				else // (A op B) op C
				{
					char op2 = genearOperador( this.operSumResMult );
					int[] n2 = getNumeros(this.resultado, op2, 0, 99);

					char op1 = genearOperador( this.operSumResMult );
					int[] n1 = getNumeros(n2[0], op1, 0, 99);

					if (n2[0] > 9)
					{
						if ((op1 == '+') && (n2[0] > 19))
						{
							if (Math.random() > 0.5D)
							{
								n1[1] = ((int)(Math.random() * 10.0D) % 10);
								n2[0] -= n1[1];
							}
							else
							{
								n1[0] = ((int)(Math.random() * 10.0D) % 10);
								n1[1] = (n2[0] - n1[0]);
							}
						}
						else if (op1 == '-')
						{
							n1[1] = ((int)(Math.random() * 10.0D) % 10);
							n1[0] = (n1[1] + n2[0]);
						}
					}

					if ((op2 == '*') && (op1 != '*'))
					{
						this.oper += "( ";
					}

					this.oper = (this.oper + n1[0] + " " + op1 + " " + n1[1] + " ");

					if ((op2 == '*') && (op1 != '*'))
					{
						this.oper += ") ";
					}

					this.oper = (this.oper + op2 + " " + n2[1] + " = ?");
				}
			}
		}
		else if (this.dificultad == 3)
		{
			int min = 0;int max = 99;
			int tipoArbol = (int)(Math.random() * 10.0D) % 6;

			char op1 = '+';char op2 = '+';char op3 = '+';
			int[] n1 = new int[]{0, 0};
			int[] n2 = new int[]{0, 0};
			int[] n3 = new int[]{0, 0};


			switch (tipoArbol)
			{
				case( 0 ): //(A op1 B) op2 (C op3 D) 
				{
					op2 = genearOperador( this.operSumRes );
					n2 = getNumeros(this.resultado, op2, min, max);
	
					op1 = genearOperador( this.operSumResMult );
					n1 = getNumeros(n2[0], op1, min, max);
	
					op3 = genearOperador( this.operSumResMult );
					n3 = getNumeros(n2[1], op3, min, max);
	
					this.oper = (n1[0] + " " + op1 + " " + n1[1] + " " + op2 + " ");
					if ((op2 == '-') && (op3 != '*'))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n3[0] + " " + op3 + " " + n3[1] + " ");
	
					if ((op2 == '-') && (op3 != '*'))
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;
				}
				case( 1 ): //A op1 (B op2 C) op3 D
				{
					op3 = genearOperador( this.operSumRes );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op1 = genearOperador( this.operSumRes );
					n1 = getNumeros(n3[0], op1, min, max);
	
					op2 = genearOperador( this.operSumResMult );
					n2 = getNumeros(n1[1], op2, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
	
					if ((op1 == '-') && (op2 != '*'))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if ((op1 == '-') && (op2 != '*'))
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
	
					break;
	
				}
				case( 2 ): //A op1 (B op2 (C op3 D))
				{
					op1 = genearOperador( this.operSumResMult );
					n1 = getNumeros(this.resultado, op1, min, max);
	
					op2 = genearOperador( this.operSumResMult );
					n2 = getNumeros(n1[1], op2, min, max);
	
					op3 = '*';//this.genearOperador( this.operMultDiv );
					n3 = getNumeros(n2[1], op3, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
					if (op1 != '+')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " ");
	
					if ((op3 != '*') && (op2 != '+'))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n3[0] + " " + op3 + " " + n3[1]);
	
					if ((op3 != '*') && (op2 != '+'))
					{
						this.oper += ") ";
					}
	
					if (op1 != '+')
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;
	
				}
				case( 3 ): //A op1 ((B op2 C) op3 D)
				{
					op1 = genearOperador( this.operSumResMult );
					n1 = getNumeros(this.resultado, op1, min, max);
	
					op3 = genearOperador( this.operSumResMult );
					n3 = getNumeros(n1[1], op3, min, max);
	
					op2 = '*';//this.genearOperador( this.operMultDiv );
					n2 = getNumeros(n3[0], op2, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
					if (op1 != '+')
					{
						this.oper += "( ";
					}
	
					if (op3 == '*')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if (op3 == '*')
					{
						this.oper += ")";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " ");
	
					if (op1 != '+')
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;
				}
				case( 4 ): //((A op1 B) op2 C) op3 D
				{
					op3 = genearOperador( this.operSumResMult );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op2 = genearOperador( this.operSumResMult );
					n2 = getNumeros(n3[0], op2, min, max);
	
					op1 = '*';//this.genearOperador( this.operMultDiv );
					n1 = getNumeros(n2[0], op1, min, max);
	
					if (op3 == '*')
					{
						this.oper = "( ";
					}
	
					if (op2 == '*')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n1[0] + " " + op1 + " " + n1[1] + " ");
	
					if (op2 == '*')
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op2 + " " + n2[1] + " ");
	
					if (op3 == '*')
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
	
					break;	  
				}
				default: //(A op1 (B op2 C)) op3 D
				{
					op3 = genearOperador( this.operSumResMult );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op1 = '*';//this.genearOperador( this.operMultDiv );
					n1 = getNumeros(n3[0], op1, min, max);
	
					op2 = '*';//this.genearOperador( this.operMultDiv );
					n2 = getNumeros(n1[1], op2, min, max);
	
					if (op3 == '*')
					{
						this.oper = "( ";
					}
	
					this.oper = (this.oper + n1[0] + " " + op1 + " ");
	
	
					if ((op1 != '+') && (op2 != '*'))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if ((op1 != '+') && (op2 != '*'))
					{
						this.oper += ") ";
					}
	
					if (op3 == '*')
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
	
					break;
				}
			}

		} 
		else 
		{
			int min = 0;int max = 99;
			int tipoArbol = (int)(Math.random() * 10.0D) % 6;

			char op1 = '+';char op2 = '+';char op3 = '+';
			int[] n1 = new int[]{0, 0};
			int[] n2 = new int[]{0, 0};
			int[] n3 = new int[]{0, 0};

			switch (tipoArbol)
			{
				case (0):
				{
					op2 = genearOperador( this.operSumRes );
					n2 = getNumeros(this.resultado, op2, min, max);
	
					op1 = genearOperador( this.operTodos );
					n1 = getNumeros(n2[0], op1, min, max);
	
					op3 = genearOperador( this.operTodos );
					n3 = getNumeros(n2[1], op3, min, max);
	
					this.oper = (n1[0] + " " + op1 + " " + n1[1] + " " + op2 + " ");
					if ((op2 == '-') && ((op3 == '+') || (op3 == '-')))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n3[0] + " " + op3 + " " + n3[1] + " ");
	
					if ((op2 == '-') && ((op3 == '+') || (op3 == '-')))
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;
	
				}
				case (1): //A op1 (B op2 C) op3 D
				{
					op3 = genearOperador( this.operSumRes );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op1 = genearOperador( this.operSumRes );
					n1 = getNumeros(n3[0], op1, min, max);
	
					op2 = genearOperador( this.operTodos );
					n2 = getNumeros(n1[1], op2, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
	
					if ((op1 == '-') && (op2 != '*') && (op2 != '/'))
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if ((op1 == '-') && (op2 != '*') && (op2 != '/'))
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
	
					break;
	
				}
				case (2): //A op1 (B op2 (C op3 D))
				{
					op1 = genearOperador( this.operTodos );
					n1 = getNumeros(this.resultado, op1, min, max);
	
					op2 = genearOperador( this.operTodos );
					n2 = getNumeros(n1[1], op2, min, max);
	
					op3 = genearOperador( this.operMultDiv );
					n3 = getNumeros(n2[1], op3, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
					if (op1 != '+')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " ");
	
					if (op2 == '/')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n3[0] + " " + op3 + " " + n3[1]);
	
					if (op2 == '/')
					{
						this.oper += ") ";
					}
	
					if (op1 != '+')
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;	
				}
				case (3):  //A op1 ((B op2 C) op3 D)
				{
					op1 = genearOperador( this.operTodos );
					n1 = getNumeros(this.resultado, op1, min, max);
	
					op3 = genearOperador( this.operTodos );
					n3 = getNumeros(n1[1], op3, min, max);
	
					op2 = genearOperador( this.operMultDiv );
					n2 = getNumeros(n3[0], op2, min, max);
	
					this.oper = (n1[0] + " " + op1 + " ");
					if (op1 != '+')
					{
						this.oper += "( ";
					}
	
					if (op2 == '/')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if (op2 == '/')
					{
						this.oper += ")";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " ");
	
					if (op1 != '+')
					{
						this.oper += ") ";
					}
	
					this.oper += "= ?";
	
					break;
	
				}
				case (4): //((A op1 B) op2 C) op3 D
				{
					op3 = genearOperador( this.operTodos );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op2 = genearOperador( this.operTodos );
					n2 = getNumeros(n3[0], op2, min, max);
	
					op1 = genearOperador( this.operMultDiv );
					n1 = getNumeros(n2[0], op1, min, max);
	
					if ((op3 == '/') || (op3 == '*'))
					{
						this.oper = "( ";
					}
	
					if (op1 == '/')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n1[0] + " " + op1 + " " + n1[1] + " ");
	
					if (op1 == '/')
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op2 + " " + n2[1] + " ");
	
					if ((op3 == '/') || (op3 == '*'))
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
	
					break;	
				}
				default:  //(A op1 (B op2 C)) op3 D
				{
					op3 = genearOperador( this.operTodos );
					n3 = getNumeros(this.resultado, op3, min, max);
	
					op1 = genearOperador( this.operMultDiv );
					n1 = getNumeros(n3[0], op1, min, max);
	
					op2 = genearOperador( this.operMultDiv );
					n2 = getNumeros(n1[1], op2, min, max);
	
					if ((op3 == '/') || (op3 == '*'))
					{
						this.oper = "( ";
					}
	
					this.oper = (this.oper + n1[0] + " " + op1 + " ");
	
	
					if (op1 == '/')
					{
						this.oper += "( ";
					}
	
					this.oper = (this.oper + n2[0] + " " + op2 + " " + n2[1] + " ");
	
					if (op1 == '/')
					{
						this.oper += ") ";
					}
	
					if ((op3 == '/') || (op3 == '*'))
					{
						this.oper += ") ";
					}
	
					this.oper = (this.oper + op3 + " " + n3[1] + " = ?");
				}
			}
		}
	}
	
	private int[] getNumeros(int valor, char op, int rgMin, int rgMax)
	{
		int[] nums = new int[]{ 0, 0 };

		nums[0] = ((int)Math.floor(Math.random() * (rgMax - rgMin + 98.0 / 99.0 ) + rgMin));

		switch (op)
		{
			case ('+'):
			{
				if (valor != 0)
				{
					nums[0] = ((int)(Math.random() * 100.0D) % valor);
					nums[1] = (valor - nums[0]);
				}
				else
				{
					nums[0] = (nums[1] = 0);
				}
	
				break;
			}
			case ('-'):
			{
				double rg = rgMax - rgMin + 98.0 / 99.0;
	
				do
				{
					nums[0] = ((int)(Math.random() * rg) + rgMin);
					nums[1] = (nums[0] - valor);
				}
				while ((nums[1] < rgMin) || (nums[1] > rgMax));
	
				break;
			}
			case ( '*' ):
			{
				if (valor != 0)
				{
					List<Integer> dfp = descomposicionFactoresPrimos( valor );
					int nfp_aux = (int)(Math.random() * 100.0D);
					nfp_aux %= dfp.size();
					int nfp = nfp_aux + 1;
	
					int n = 1;
					Iterator<Integer> it = dfp.iterator();
					while ((it.hasNext()) && (nfp > 0))
					{
						n *= ((Integer)it.next()).intValue();
						nfp--;
					}
	
					nums[0] = n;
					nums[1] = (valor / n);
	
	
				}
				else
				{
					if (nums[0] != 0)
					{
						nums[1] = 0;
	
					}
					else
					{
						nums[1] = ((int)Math.round(Math.random() * (rgMax - rgMin + 98.0 / 99.0) + rgMin));
					}
				}
	
	
				break;
			} 
			default:
			{
				while (nums[1] == 0)
				{
					if (valor != 0)
					{
						int limDiv = (rgMax - rgMin) / valor;
						nums[1] = ((int)(Math.random() * 100.0D) % limDiv + 1);
	
					}
					else
					{
						nums[1] = (nums[0] + 1);
					}
				}
	
				if (nums[1] * 2 > 99)
				{
					nums[1] -= 1;
				}
	
				nums[0] = (nums[1] * valor);
			}
		}

		return nums;
	}
}