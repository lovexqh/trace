package com.trace.hadoop.hadoopinternal.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIQueryStatus extends Remote {

    RMIFileStatus getFileStatus(String filename) throws RemoteException;
}
