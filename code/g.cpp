#include<stdio.h>
#include<string.h>

int main(int argc,char const *argv[]){
	char line[] = "hello";
	
	printf("strlen=%lu\n",strlen(line));
	printf("streof=%lu\n",sizeof(line));
	return 0;
}
