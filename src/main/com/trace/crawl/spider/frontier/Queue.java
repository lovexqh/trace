package com.trace.crawl.spider.frontier;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 队列，保存将要访问的URL
 */
public class Queue {
	private Log log = LogFactory.getLog(getClass());
	// 使用链表实现队列
	private LinkedList<Object> queue = new LinkedList<Object>();

	// 入队列
	public synchronized void enQueue(Object t) {
		queue.addLast(t);
	}

	// 出队列
	public synchronized Object deQueue() {
		return queue.removeFirst();
	}

	// 判断队列是否为空
	public synchronized boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// 判断队列是否包含t
	public synchronized boolean contians(Object t) {
		return queue.contains(t);
	}

	public synchronized boolean empty() {
		return queue.isEmpty();
	}

}