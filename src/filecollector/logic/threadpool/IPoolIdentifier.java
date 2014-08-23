package filecollector.logic.threadpool;

public interface IPoolIdentifier extends IWorker {
	void transferNewIdentifier(PoolIdentifier poolId);
	//TODO FALSCH: Nur wg. zusaetzlicher Info fuer Log4j.msg sollte wieder raus!!
	String infoStrFALSCH();
}
