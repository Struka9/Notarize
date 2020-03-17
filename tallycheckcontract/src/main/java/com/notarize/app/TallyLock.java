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
    private static final String BINARY = "608060405260006100176001600160e01b0361006616565b600080546001600160a01b0319166001600160a01b0383169081178255604051929350917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908290a35061006a565b3390565b610c99806100796000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80638da5cb5b116100665780638da5cb5b146102175780638f32d59b1461023b578063a1347bfa14610257578063e03d958214610370578063f2fde38b1461041457610093565b806340a141ff146100985780634d238c8e146100c0578063715018a6146100e65780638d58bf02146100ee575b600080fd5b6100be600480360360208110156100ae57600080fd5b50356001600160a01b031661043a565b005b6100be600480360360208110156100d657600080fd5b50356001600160a01b03166104ca565b6100be6105a5565b6100be6004803603604081101561010457600080fd5b810190602081018135600160201b81111561011e57600080fd5b82018360208201111561013057600080fd5b803590602001918460018302840111600160201b8311171561015157600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b8111156101a357600080fd5b8201836020820111156101b557600080fd5b803590602001918460018302840111600160201b831117156101d657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610636945050505050565b61021f610879565b604080516001600160a01b039092168252519081900360200190f35b610243610889565b604080519115158252519081900360200190f35b6102fb6004803603602081101561026d57600080fd5b810190602081018135600160201b81111561028757600080fd5b82018360208201111561029957600080fd5b803590602001918460018302840111600160201b831117156102ba57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506108ad945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561033557818101518382015260200161031d565b50505050905090810190601f1680156103625780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102fb6004803603602081101561038657600080fd5b810190602081018135600160201b8111156103a057600080fd5b8201836020820111156103b257600080fd5b803590602001918460018302840111600160201b831117156103d357600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506109a1945050505050565b6100be6004803603602081101561042a57600080fd5b50356001600160a01b0316610a47565b610442610889565b610481576040805162461bcd60e51b81526020600482018190526024820152600080516020610bfd833981519152604482015290519081900360640190fd5b6001600160a01b038116600081815260016020526040808220805460ff19169055517f16f841e5082ce78af450ac59cdac0c05e84bc4539a6578ab59be104d07fa34aa9190a250565b6104d2610889565b610511576040805162461bcd60e51b81526020600482018190526024820152600080516020610bfd833981519152604482015290519081900360640190fd5b6001600160a01b0381166105565760405162461bcd60e51b8152600401808060200182810382526023815260200180610c426023913960400191505060405180910390fd5b6001600160a01b0381166000818152600160208190526040808320805460ff1916909217909155517f4abba4877a811c10c94281a6a64a19db457ed00ca5e9c8dcd2563023d04771399190a250565b6105ad610889565b6105ec576040805162461bcd60e51b81526020600482018190526024820152600080516020610bfd833981519152604482015290519081900360640190fd5b600080546040516001600160a01b03909116907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0908390a3600080546001600160a01b0319169055565b3360009081526001602052604090205460ff1661069a576040805162461bcd60e51b815260206004820152601e60248201527f5468652073656e646572206d75737420626520612076616c696461746f720000604482015290519081900360640190fd5b60008251116106f0576040805162461bcd60e51b815260206004820152601d60248201527f446f63756d656e7420686173682063616e6e6f7420626520656d707479000000604482015290519081900360640190fd5b60008151116107305760405162461bcd60e51b8152600401808060200182810382526025815260200180610c1d6025913960400191505060405180910390fd5b806002836040518082805190602001908083835b602083106107635780518252601f199092019160209182019101610744565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516107a49591949190910192509050610b3e565b50336000908152600360209081526040822080546001810180835591845292829020855191936107da9391019190860190610b3e565b5050604080516020808252845181830152845133937fe0ac180b4f5d172938a3fde61715fbfd4686c7f7a24387e7800efc968bde74379387939092839283019185019080838360005b8381101561083b578181015183820152602001610823565b50505050905090810190601f1680156108685780820380516001836020036101000a031916815260200191505b509250505060405180910390a25050565b6000546001600160a01b03165b90565b600080546001600160a01b031661089e610a9a565b6001600160a01b031614905090565b60606002826040518082805190602001908083835b602083106108e15780518252601f1990920191602091820191016108c2565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f60026001831615909802909501169590950492830182900482028801820190528187529294509250508301828280156109955780601f1061096a57610100808354040283529160200191610995565b820191906000526020600020905b81548152906001019060200180831161097857829003601f168201915b50505050509050919050565b80516020818301810180516002808352938301948301949094209390528254604080516001831615610100026000190190921693909304601f8101839004830282018301909352828152929190830182828015610a3f5780601f10610a1457610100808354040283529160200191610a3f565b820191906000526020600020905b815481529060010190602001808311610a2257829003601f168201915b505050505081565b610a4f610889565b610a8e576040805162461bcd60e51b81526020600482018190526024820152600080516020610bfd833981519152604482015290519081900360640190fd5b610a9781610a9e565b50565b3390565b6001600160a01b038116610ae35760405162461bcd60e51b8152600401808060200182810382526026815260200180610bd76026913960400191505060405180910390fd5b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b7f57805160ff1916838001178555610bac565b82800160010185558215610bac579182015b82811115610bac578251825591602001919060010190610b91565b50610bb8929150610bbc565b5090565b61088691905b80821115610bb85760008155600101610bc256fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f20616464726573734f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657253696e676e61747572652042756e646c652055524c2063616e6e6f7420626520656d70747943616e6e6f74206164642030783020616464726573732061732076616c696461746f72a265627a7a72315820288beb1e06af2be20648f6802bebe14e3799040782788c83f8d907c78c86f01564736f6c634300050d0032";

    public static final String FUNC__DOCUMENTSTOSIGNATUREBUNDLE = "_documentsToSignatureBundle";

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
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGDOCUMENTSIGNED_EVENT, transactionReceipt);
        ArrayList<LogDocumentSignedEventResponse> responses = new ArrayList<LogDocumentSignedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
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
                EventValuesWithLog eventValues = extractEventParametersWithLog(LOGDOCUMENTSIGNED_EVENT, log);
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
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGVALIDATORADDED_EVENT, transactionReceipt);
        ArrayList<LogValidatorAddedEventResponse> responses = new ArrayList<LogValidatorAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
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
                EventValuesWithLog eventValues = extractEventParametersWithLog(LOGVALIDATORADDED_EVENT, log);
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
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGVALIDATORREMOVED_EVENT, transactionReceipt);
        ArrayList<LogValidatorRemovedEventResponse> responses = new ArrayList<LogValidatorRemovedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
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
                EventValuesWithLog eventValues = extractEventParametersWithLog(LOGVALIDATORREMOVED_EVENT, log);
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
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
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
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
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
                Arrays.<Type>asList(new Utf8String(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addValidator(String _validator) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDVALIDATOR, 
                Arrays.<Type>asList(new Address(160, _validator)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getSignatureBundleUrl(String _documentHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSIGNATUREBUNDLEURL, 
                Arrays.<Type>asList(new Utf8String(_documentHash)),
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
                Arrays.<Type>asList(new Address(160, _validator)),
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
                Arrays.<Type>asList(new Utf8String(_documentHash),
                new Utf8String(_bundleUrl)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(160, newOwner)),
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
