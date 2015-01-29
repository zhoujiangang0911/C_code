#include <stdio.h>

int main(){

int a=0;

scanf("%d",&a);
if(a<20){
   int	b=1;
}
int c = a%10;
int b = a/10;





switch(b){

case 1:printf(" unreadable.");break;

case 2:printf(" barely readable,occasional words distinguishable.");break;

case 3:printf(" readable with considerable diffculty.");break;

case 4:printf(" readable with practically on difficulty.");break;

case 5:printf(" perfectly readable.");break;

}
switch (c){

case 1:printf("Faint signals,barely perceptible,  ");break;

case 2:printf("Very weak signals,  ");break;

case 3:printf("Weak signals,  ");break;

case 4:printf("Fair signals, ");break;

case 5:printf("Fairly good signals,  ");break;

case 6:printf("Good signals, ");break;

case 7:printf("Moderately strong signals, ");break;

case 8:printf("Extremely strong signals, ");break;

case 9:printf("Very weak signals, ");break;

}



return 0;

}



