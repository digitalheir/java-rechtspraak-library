//package org.crf.utilities.log4j;
//
//import java.util.Enumeration;
//
//import org.apache.logging.log4j.core.Appender;
//
//import org.slf4j.Logger;
//
///**
// * Initializes Log4j: log messages will be printed to screen with prefix of "INFO - ", "DEBUG - ", etc.
// *
// * @author Asher Stern
// * Date: Nov 4, 2014
// *
// */
//public class Log4jInit
//{
//
//	public static void init()
//	{
//		init(Level.INFO);
//	}
//
//	public static void init(Level level)
//	{
//		if (!alreadyInitialized)
//		{
//			synchronized(Log4jInit.class)
//			{
//				if (!alreadyInitialized)
//				{
//
//						Logger.getRootLogger().setLevel(level);
//
//					Enumeration<?> enumAppenders = Logger.getRootLogger().getAllAppenders();
//					while (enumAppenders.hasMoreElements())
//					{
//						Appender appender = (Appender) enumAppenders.nextElement();
//						appender.setLayout(new VerySimpleLayout());
//					}
//
//					alreadyInitialized = true;
//				}
//			}
//		}
//
//	}
//
//	private static boolean alreadyInitialized = false;
//}
