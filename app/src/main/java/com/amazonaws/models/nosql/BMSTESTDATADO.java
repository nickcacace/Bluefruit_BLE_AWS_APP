package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "bmstest-mobilehub-2022040187-BMS_TEST_DATA")

public class BMSTESTDATADO {
    private Double _vALUE;
    private Double _tIME;

    @DynamoDBHashKey(attributeName = "VALUE")
    @DynamoDBAttribute(attributeName = "VALUE")
    public Double getVALUE() {
        return _vALUE;
    }

    public void setVALUE(final Double _vALUE) {
        this._vALUE = _vALUE;
    }
    @DynamoDBRangeKey(attributeName = "TIME")
    @DynamoDBAttribute(attributeName = "TIME")
    public Double getTIME() {
        return _tIME;
    }

    public void setTIME(final Double _tIME) {
        this._tIME = _tIME;
    }

}
