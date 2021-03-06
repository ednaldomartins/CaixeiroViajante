
package model;

import java.util.Random;
import util.FuncoesVetor;
import util.Metaheuristica;

/*******************************************************************************
 * @author Ednaldo                                                             *
 *  date: 04.11.2018                                                           *
 ******************************************************************************/
public class IteratedLocalSearch implements Metaheuristica 
{
    private Random random = new Random();
    private int nivelPerturbacao = 1;
    
    @Override
    public void explorar(Rota r, long[][] grafo, int[] rota) 
    {
        long melhorSolucao = r.getMelhorResultado();
        long maiorAresta = Long.MIN_VALUE;
        int i, pos = 0, cont = 0;
        while(cont++ < 100)
        {
            //pertubacao procurando uma aresta grande e trocando por outra possivel
            for(i = rota.length-1; i > 1; i--)
            {
                if(grafo[rota[i-1]][rota[i]] > maiorAresta)
                {
                    maiorAresta = grafo[rota[i-1]][rota[i]];
                    pos = i;
                }
            }
            
            perturbacao(rota, pos, rota.length);
            nivelPerturbacao = (melhorSolucao == r.getMelhorResultado() && nivelPerturbacao < 4) ? nivelPerturbacao+1 : nivelPerturbacao;
            nivelPerturbacao = (melhorSolucao != r.getMelhorResultado() && nivelPerturbacao > 1) ? nivelPerturbacao-1 : nivelPerturbacao;
            new VND().refinar(r, grafo, rota);
        }
    }
    
    public void perturbacao (int [] list, int i, int tamanhoVetor)
    {
        switch(nivelPerturbacao)
        {
            case 1:
                perturbacao1(list, i, random.nextInt(tamanhoVetor-3)+1);
                break;
            case 2:
                perturbacao2(list, i, random.nextInt(tamanhoVetor-3)+1);
                break;
            case 3:
                perturbacao3(list, i, random.nextInt(tamanhoVetor-3)+1);
                break;
            case 4:
                perturbacao3(list, random.nextInt(tamanhoVetor-3)+1, random.nextInt(tamanhoVetor-3)+1);
        }
    }
    
    //Insert
    private void perturbacao1(int [] list, int i, int j)
    {
        int temp = list[i];
        if( i < j )
            while(i < j)
                list[i] = list[++i];
        else
            while(i > j)
                list[i] = list[--i];
        list[j] = temp;
    }

    //Insert + Exchange
    private void perturbacao2(int [] list, int i, int j) 
    {
        int temp = list[j];
        list[j] = list[i];
        list[i] = temp;
        perturbacao1(list, j, random.nextInt(list.length -2)+1);
    }
    
    //Insert + Exchange + Cross
    private void perturbacao3(int [] list, int i, int j)
    {
        perturbacao2(list, i, j);
        int k = i, l = j;
        int [] listCopia = new int[list.length];
        FuncoesVetor.copiarVetor(list, listCopia);
        
        if(i < j)
        {
            while(j < list.length-2)
                list[++i] = listCopia[++j];
            while(i < list.length-2)
                list[++i] = listCopia[++k];
        }
        else if(i > j)
        {
            while(k < list.length-2)
                list[++j] = listCopia[++k];
            while(j < list.length-2)
                list[++j] = listCopia[++l];
        }
        
    }
    
}
