#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/wait.h>
int main()
{
	
//read prepared fake response file
char buff_resp[256];
FILE *fp;
if((fp=fopen("resp_http.txt","r"))==NULL){
	perror("fail to read resp_http");
	exit(1);
}
fread(buff_resp,sizeof(buff_resp),1,fp);
printf("%s\r\n",buff_resp);
fclose(fp);

int sockfd,new_fd;
struct sockaddr_in my_addr;
struct sockaddr_in their_addr;
int sin_size;
//建立TCP套接口
if((sockfd = socket(AF_INET,SOCK_STREAM,0))==-1)
{
printf("create socket error");
perror("socket");
exit(1);
}
//初始化结构体，并绑定8080端口
my_addr.sin_family = AF_INET;    //AF_INET : IPv4 format
my_addr.sin_port = htons(9001);    //htons : 主机字节序-> 网络字节序
my_addr.sin_addr.s_addr = INADDR_ANY;   //所有本地地址 
bzero(&(my_addr.sin_zero),8);
//绑定套接口
if(bind(sockfd,(struct sockaddr *)&my_addr,sizeof(struct sockaddr))==-1)
{
perror("bind socket error");
exit(1);
}
//创建监听套接口
if(listen(sockfd,10)==-1)
{
perror("listen");
exit(1);
}
//等待连接
while(1)
{
sin_size = sizeof(struct sockaddr_in);
printf("server is run.\n");
//如果建立连接，将产生一个全新的套接字
if((new_fd = accept(sockfd,(struct sockaddr *)&their_addr,&sin_size))==-1)
{
perror("accept");
exit(1);
}
printf("accept success.\n");
//生成一个子进程来完成和客户端的会话，父进程继续监听
if(!fork())
{
printf("create new thred success.\n");
//读取客户端发来的信息
int numbytes;
char buff[256];
memset(buff,0,256);
if((numbytes = recv(new_fd,buff,sizeof(buff),0))==-1)
{
perror("recv");
exit(1);
}
printf("%s \r\n",buff);
//将从客户端接收到的信息再发回客户端


printf("\r\n %s \r\n",buff_resp);

if(send(new_fd,buff_resp,strlen(buff_resp),0)==-1)
perror("send");

close(new_fd);
exit(0);
}
close(new_fd);
}
close(sockfd);
}