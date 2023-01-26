// IFastIPCService.aidl
package com.lay.fastipc;
// Declare any non-default types here with import statements
import com.lay.fastipc.model.Response;
import com.lay.fastipc.model.Request;

interface IFastIPCService {
    Response send(in Request request);
}