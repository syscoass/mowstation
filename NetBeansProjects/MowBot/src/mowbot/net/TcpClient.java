/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

import mowbot.CommonInterface;

import java.net.*;
/**
 *
 * @author justin.hawkins
 */
public class TcpClient extends CommonInterface {


/**
29   * Sends one message when a connection is open and echoes back any received
30   * data to the server.  Simply put, the echo client initiates the ping-pong
31   * traffic between the echo client and server by sending the first message to
32   * the server.
33   *
34   * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
35   * @author <a href="http://gleamynode.net/">Trustin Lee</a>
36   *
37   * @version $Rev: 2080 $, $Date: 2010-01-26 18:04:19 +0900 (Tue, 26 Jan 2010) $
38   *
39   */
        String ip_;
        int port_;
        ClientBootstrap bootstrap;
        ChannelFuture future;
        TcpClientHandler handler;
        Channel channel_;

        public void TcpClient()
        {

        }

        public boolean initialize(String ip, int port)
        {
            ip_ = ip;
            port_ = port;

          // Configure the client.
          bootstrap = new ClientBootstrap(
                  new NioClientSocketChannelFactory(
                          Executors.newCachedThreadPool(),
                          Executors.newCachedThreadPool()));

          // Set up the pipeline factory.
          bootstrap.setPipelineFactory(new TcpClientPipelineFactory());
          
          // Start the connection attempt.
          future = bootstrap.connect(new InetSocketAddress(ip_, port_));

          // Wait until the connection attempt succeeds or fails.
          channel_ = future.awaitUninterruptibly().getChannel();
          if (!future.isSuccess()) {
              future.getCause().printStackTrace();
              bootstrap.releaseExternalResources();
             return false;
          }

            return true;
        }


    @Override
    public synchronized void writeData(String data){
          //e.getChannel().write(e.getMessage());
          channel_.write(data);
      }

      public synchronized void shutdown(){
          
          // Shut down thread pools to exit.
          bootstrap.releaseExternalResources();
      }
}
