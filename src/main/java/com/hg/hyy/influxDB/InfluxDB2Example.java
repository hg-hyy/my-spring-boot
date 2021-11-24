package com.hg.hyy.influxDB;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * @author littl
 * @description InfluxDB2
 * @email 1021509854@qq.com
 * @date 2021-11-24 14:47:58
 */
@Service
public class InfluxDB2Example {

  private final InfluxDBClient influxDBClient;

  public InfluxDB2Example() {
    // You can generate a Token from the "Tokens Tab" in the UI
    String token =
        "2QgWrkVnl8OapyKkMxZ3YNhxQxkJfflvuyWdfRE3NO-kEyeIiD6u8sWh9GONMGvyI4wH2ZbQaV8j4I2Sno5ZoA==";
    String org = "hG";
    String bucket = "hg";
    this.influxDBClient =
        InfluxDBClientFactory.create(
            "http://192.168.118.143:30086", token.toCharArray(), org, bucket);
  }

  public void writeData() {
    // Write data
    //
    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

    //
    // Write by Data Point
    //
    Point point =
        Point.measurement("temperature")
            .addTag("localhost", "127.0.0.1")
            .addField("TP100", 23.43)
            .time(Instant.now().toEpochMilli(), WritePrecision.MS);

    writeApi.writePoint(point);

    //
    // Write by LineProtocol
    //
    writeApi.writeRecord(WritePrecision.NS, "temperature,location=north value=60.0");

    //
    // Write by POJO
    //
    Temperature temperature = new Temperature();
    temperature.location = "south";
    temperature.value = 62D;
    temperature.time = Instant.now();

    writeApi.writeMeasurement(WritePrecision.NS, temperature);
  }

  public void readData() {
    //
    // Query data
    //
    String flux = "from(bucket:\"hg\") |> range(start: 0)";

    QueryApi queryApi = influxDBClient.getQueryApi();

    List<FluxTable> tables = queryApi.query(flux);
    for (FluxTable fluxTable : tables) {
      List<FluxRecord> records = fluxTable.getRecords();
      for (FluxRecord fluxRecord : records) {
        System.out.println(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
      }
    }
  }

  public void close() {
    influxDBClient.close();
  }

  @Measurement(name = "temperature")
  private static class Temperature {

    @Column(tag = true)
    String location;

    @Column Double value;

    @Column(timestamp = true)
    Instant time;
  }

  @Measurement(name = "mem")
  private static class Mem {
    @Column(tag = true)
    String host;

    @Column Double used_percent;

    @Column(timestamp = true)
    Instant time;
  }
}
