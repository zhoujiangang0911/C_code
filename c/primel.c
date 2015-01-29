#include<stdio.h>
#include<math.h>
#define SIZE 100
int prime2(int a[],int n){
 	int i,j,k=0,flag;
 	
	for(i =2;i<=n;i++){
	flag = 1;
		for(j=2;j<=sqrt(i)&&flag;j++){
		 	if(i%j==0){
			flag =0;
			}
			if(flag ==1){
			a[k]=i;
			k++;
			}
		}
		return k;
	}
void main(){
	int i,k,a[SIZE/2];
	k = primel(a,SIZE);
	printf("\n");
	for(i=0;i<=k-1;i++){
		printf("%-4d",a[i]);	
	
		}

		
	
}










}
