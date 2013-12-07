SocketServer这个项目是主程序。
实现了http服务器和servlet容器。
仅仅实现了reponse.getWriter().print("")这种输出，没有实现getAttribute()那些。
连POST都没有实现（因为没有解析）
只实现了GET
想要更完善的话需要修改response类和request类，还需要加 session类
运行Start类开始
