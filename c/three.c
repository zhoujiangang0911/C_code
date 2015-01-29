#include<stdio.h>
int main()
{
int a;
printf("请输入3位正整数\n");
scanf("%d",&a);
int b = (a%10)*100+((a-(a/100)*100)/10)*10+(a/100);
printf("输出的逆序的数\n%d",b);
return 0;
}

