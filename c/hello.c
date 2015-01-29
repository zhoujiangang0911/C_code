#include<stdio.h>
int main()
{
	int price =0;  
	int amount = 100;
   	printf(" please input you price \n");
	

	scanf("%d",&price);

	printf("请输入票面");
	scanf("%d",&amount);
	
	int change = amount-price;
	
	printf("you money%d\n" ,change);
	return 0;
}
