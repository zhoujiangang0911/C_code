#include<stdio.h>
int guess1(int x){
	int i;
	for(i=0;i<=100;i++){
		if(i==x)
		 return i;
			
	return i;	
	}


}
int guess2(int x){
int i=0;
int low=1;
int high=100;
int middle;
	do{
		i++;
		middle = (low+high)/2;
		if(middle==x){
			return i;
		}else if(middle<x){
			low = middle+1;
		}else{
			high = middle-1;
		}
	
	}while(1);
}
void main(){
 printf("\nguess1 i = %d",guess1(67));
printf("\nguess2 i = %d",guess2(67));
printf("\nguess1 i = %d",guess1(15));
printf("\nguess2 i = %d",guess2(15));
	
}
