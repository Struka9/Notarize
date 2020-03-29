package com.notarize.app;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.7.
 */
@SuppressWarnings("rawtypes")
public class TallyLock extends Contract {
    private static final String BINARY = "608060405260006100176001600160e01b0361006616565b600080546001600160a01b0319166001600160a01b0383169081178255604051929350917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908290a35061006a565b3390565b610d51806100796000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638da5cb5b116100665780638da5cb5b146102c35780638f32d59b146102e7578063a1347bfa14610303578063e03d9582146103a7578063f2fde38b1461044b5761009e565b806320a7d292146100a357806340a141ff146101445780634d238c8e1461016c578063715018a6146101925780638d58bf021461019a575b600080fd5b6100cf600480360360408110156100b957600080fd5b506001600160a01b038135169060200135610471565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101095781810151838201526020016100f1565b50505050905090810190601f1680156101365780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61016a6004803603602081101561015a57600080fd5b50356001600160a01b0316610525565b005b61016a6004803603602081101561018257600080fd5b50356001600160a01b03166105b5565b61016a610690565b61016a600480360360408110156101b057600080fd5b810190602081018135600160201b8111156101ca57600080fd5b8201836020820111156101dc57600080fd5b803590602001918460018302840111600160201b831117156101fd57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561024f57600080fd5b82018360208201111561026157600080fd5b803590602001918460018302840111600160201b8311171561028257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610721945050505050565b6102cb610964565b604080516001600160a01b039092168252519081900360200190f35b6102ef610974565b604080519115158252519081900360200190f35b6100cf6004803603602081101561031957600080fd5b810190602081018135600160201b81111561033357600080fd5b82018360208201111561034557600080fd5b803590602001918460018302840111600160201b8311171561036657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610998945050505050565b6100cf600480360360208110156103bd57600080fd5b810190602081018135600160201b8111156103d757600080fd5b8201836020820111156103e957600080fd5b803590602001918460018302840111600160201b8311171561040a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610a8c945050505050565b61016a6004803603602081101561046157600080fd5b50356001600160a01b0316610aff565b6003602052816000526040600020818154811061048a57fe5b600091825260209182902001805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815294509092509083018282801561051d5780601f106104f25761010080835404028352916020019161051d565b820191906000526020600020905b81548152906001019060200180831161050057829003601f168201915b505050505081565b61052d610974565b61056c576040805162461bcd60e51b81526020600482018190526024820152600080516020610cb5833981519152604482015290519081900360640190fd5b6001600160a01b038116600081815260016020526040808220805460ff19169055517f16f841e5082ce78af450ac59cdac0c05e84bc4539a6578ab59be104d07fa34aa9190a250565b6105bd610974565b6105fc576040805162461bcd60e51b81526020600482018190526024820152600080516020610cb5833981519152604482015290519081900360640190fd5b6001600160a01b0381166106415760405162461bcd60e51b8152600401808060200182810382526023815260200180610cfa6023913960400191505060405180910390fd5b6001600160a01b0381166000818152600160208190526040808320805460ff1916909217909155517f4abba4877a811c10c94281a6a64a19db457ed00ca5e9c8dcd2563023d04771399190a250565b610698610974565b6106d7576040805162461bcd60e51b81526020600482018190526024820152600080516020610cb5833981519152604482015290519081900360640190fd5b600080546040516001600160a01b03909116907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908390a3600080546001600160a01b0319169055565b3360009081526001602052604090205460ff16610785576040805162461bcd60e51b815260206004820152601e60248201527f5468652073656e646572206d75737420626520612076616c696461746f720000604482015290519081900360640190fd5b60008251116107db576040805162461bcd60e51b815260206004820152601d60248201527f446f63756d656e7420686173682063616e6e6f7420626520656d707479000000604482015290519081900360640190fd5b600081511161081b5760405162461bcd60e51b8152600401808060200182810382526025815260200180610cd56025913960400191505060405180910390fd5b806002836040518082805190602001908083835b6020831061084e5780518252601f19909201916020918201910161082f565b51815160209384036101000a6000190180199092169116179052920194855250604051938490038101909320845161088f9591949190910192509050610bf6565b50336000908152600360209081526040822080546001810180835591845292829020855191936108c59391019190860190610bf6565b5050604080516020808252845181830152845133937fe0ac180b4f5d172938a3fde61715fbfd4686c7f7a24387e7800efc968bde74379387939092839283019185019080838360005b8381101561092657818101518382015260200161090e565b50505050905090810190601f1680156109535780820380516001836020036101000a031916815260200191505b509250505060405180910390a25050565b6000546001600160a01b03165b90565b600080546001600160a01b0316610989610b52565b6001600160a01b031614905090565b60606002826040518082805190602001908083835b602083106109cc5780518252601f1990920191602091820191016109ad565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f6002600183161590980290950116959095049283018290048202880182019052818752929450925050830182828015610a805780601f10610a5557610100808354040283529160200191610a80565b820191906000526020600020905b815481529060010190602001808311610a6357829003601f168201915b50505050509050919050565b80516020818301810180516002808352938301948301949094209390528254604080516001831615610100026000190190921693909304601f810183900483028201830190935282815292919083018282801561051d5780601f106104f25761010080835404028352916020019161051d565b610b07610974565b610b46576040805162461bcd60e51b81526020600482018190526024820152600080516020610cb5833981519152604482015290519081900360640190fd5b610b4f81610b56565b50565b3390565b6001600160a01b038116610b9b5760405162461bcd60e51b8152600401808060200182810382526026815260200180610c8f6026913960400191505060405180910390fd5b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c3757805160ff1916838001178555610c64565b82800160010185558215610c64579182015b82811115610c64578251825591602001919060010190610c49565b50610c70929150610c74565b5090565b61097191905b80821115610c705760008155600101610c7a56fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f20616464726573734f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657253696e676e61747572652042756e646c652055524c2063616e6e6f7420626520656d70747943616e6e6f74206164642030783020616464726573732061732076616c696461746f72a265627a7a723158200622f030f9664c566516cb28a2818774b5b492e12e7692858be1c8c999582b2e64736f6c634300050d0032";

    public static final String FUNC__DOCUMENTSTOSIGNATUREBUNDLE = "_documentsToSignatureBundle";

    public static final String FUNC__VALIDATORTODOCUMENTS = "_validatorToDocuments";

    public static final String FUNC_ADDVALIDATOR = "addValidator";

    public static final String FUNC_GETSIGNATUREBUNDLEURL = "getSignatureBundleUrl";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REMOVEVALIDATOR = "removeValidator";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SIGNDOCUMENT = "signDocument";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event LOGDOCUMENTSIGNED_EVENT = new Event("LogDocumentSigned", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGVALIDATORADDED_EVENT = new Event("LogValidatorAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event LOGVALIDATORREMOVED_EVENT = new Event("LogValidatorRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected TallyLock(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TallyLock(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TallyLock(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TallyLock(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<LogDocumentSignedEventResponse> getLogDocumentSignedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGDOCUMENTSIGNED_EVENT, transactionReceipt);
        ArrayList<LogDocumentSignedEventResponse> responses = new ArrayList<LogDocumentSignedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogDocumentSignedEventResponse typedResponse = new LogDocumentSignedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogDocumentSignedEventResponse> logDocumentSignedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, LogDocumentSignedEventResponse>() {
            @Override
            public LogDocumentSignedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGDOCUMENTSIGNED_EVENT, log);
                LogDocumentSignedEventResponse typedResponse = new LogDocumentSignedEventResponse();
                typedResponse.log = log;
                typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogDocumentSignedEventResponse> logDocumentSignedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGDOCUMENTSIGNED_EVENT));
        return logDocumentSignedEventFlowable(filter);
    }

    public List<LogValidatorAddedEventResponse> getLogValidatorAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGVALIDATORADDED_EVENT, transactionReceipt);
        ArrayList<LogValidatorAddedEventResponse> responses = new ArrayList<LogValidatorAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogValidatorAddedEventResponse typedResponse = new LogValidatorAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogValidatorAddedEventResponse> logValidatorAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, LogValidatorAddedEventResponse>() {
            @Override
            public LogValidatorAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGVALIDATORADDED_EVENT, log);
                LogValidatorAddedEventResponse typedResponse = new LogValidatorAddedEventResponse();
                typedResponse.log = log;
                typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogValidatorAddedEventResponse> logValidatorAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGVALIDATORADDED_EVENT));
        return logValidatorAddedEventFlowable(filter);
    }

    public List<LogValidatorRemovedEventResponse> getLogValidatorRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGVALIDATORREMOVED_EVENT, transactionReceipt);
        ArrayList<LogValidatorRemovedEventResponse> responses = new ArrayList<LogValidatorRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogValidatorRemovedEventResponse typedResponse = new LogValidatorRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogValidatorRemovedEventResponse> logValidatorRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, LogValidatorRemovedEventResponse>() {
            @Override
            public LogValidatorRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGVALIDATORREMOVED_EVENT, log);
                LogValidatorRemovedEventResponse typedResponse = new LogValidatorRemovedEventResponse();
                typedResponse.log = log;
                typedResponse._validator = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogValidatorRemovedEventResponse> logValidatorRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGVALIDATORREMOVED_EVENT));
        return logValidatorRemovedEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<String> _documentsToSignatureBundle(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__DOCUMENTSTOSIGNATUREBUNDLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> _validatorToDocuments(String param0, BigInteger param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__VALIDATORTODOCUMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addValidator(String _validator) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDVALIDATOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _validator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getSignatureBundleUrl(String _documentHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSIGNATUREBUNDLEURL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeValidator(String _validator) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEVALIDATOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _validator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> signDocument(String _documentHash, String _bundleUrl) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SIGNDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash), 
                new org.web3j.abi.datatypes.Utf8String(_bundleUrl)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static TallyLock load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TallyLock(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TallyLock load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TallyLock(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TallyLock load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TallyLock(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TallyLock load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TallyLock(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TallyLock> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TallyLock.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TallyLock> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TallyLock.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TallyLock> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TallyLock.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TallyLock> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TallyLock.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class LogDocumentSignedEventResponse extends BaseEventResponse {
        public String _validator;

        public String _documentHash;
    }

    public static class LogValidatorAddedEventResponse extends BaseEventResponse {
        public String _validator;
    }

    public static class LogValidatorRemovedEventResponse extends BaseEventResponse {
        public String _validator;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
