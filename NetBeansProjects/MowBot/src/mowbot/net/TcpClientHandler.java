/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot.net;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;


/**
31   * Handler implementation for the echo client.  It initiates the ping-pong
32   * traffic between the echo client and server by sending the first message to
33   * the server.
34   *
35   * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
36   * @author <a href="http://gleamynode.net/">Trustin Lee</a>
37   *
38   * @version $Rev: 2121 $, $Date: 2010-02-02 09:38:07 +0900 (Tue, 02 Feb 2010) $
  */
public class TcpClientHandler extends SimpleChannelUpstreamHandler {

      private static final Logger logger = Logger.getLogger(
              TcpClientHandler.class.getName());

      @Override
      public void handleUpstream(
             ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
          if (e instanceof ChannelStateEvent) {
              logger.info(e.toString());
          }
          super.handleUpstream(ctx, e);
      }

      @Override
      public void messageReceived(
              ChannelHandlerContext ctx, MessageEvent e) {
          // Print out the line received from the server.
          System.err.println(e.getMessage());
      }

      @Override
      public void exceptionCaught(
              ChannelHandlerContext ctx, ExceptionEvent e) {
          logger.log(
                  Level.WARNING,
                  "Unexpected exception from downstream.",
                 e.getCause());
        e.getChannel().close();
      }
  }