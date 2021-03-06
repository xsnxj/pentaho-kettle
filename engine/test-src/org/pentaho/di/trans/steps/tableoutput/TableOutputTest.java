package org.pentaho.di.trans.steps.tableoutput;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepPartitioningMeta;
import org.pentaho.di.trans.steps.combinationlookup.CombinationLookup;
import org.pentaho.di.trans.steps.combinationlookup.CombinationLookupData;
import org.pentaho.di.trans.steps.combinationlookup.CombinationLookupMeta;
import org.pentaho.di.trans.steps.dimensionlookup.DimensionLookup;
import org.pentaho.di.trans.steps.dimensionlookup.DimensionLookupData;
import org.pentaho.di.trans.steps.dimensionlookup.DimensionLookupMeta;

import java.sql.Connection;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class TableOutputTest {
  private DatabaseMeta databaseMeta;

  private StepMeta stepMeta;

  private TableOutput tableOutput, tableOutputSpy;
  private TableOutputMeta tableOutputMeta, tableOutputMetaSpy;
  private TableOutputData tableOutputData, tableOutputDataSpy;
  
  @Before 
  public void setUp() throws Exception {
    databaseMeta = mock( DatabaseMeta.class );
    doReturn( "" ).when( databaseMeta ).quoteField( anyString() );


    tableOutputMeta = mock( TableOutputMeta.class );
    doReturn( databaseMeta ).when( tableOutputMeta ).getDatabaseMeta();

    stepMeta = mock( StepMeta.class );
    doReturn( "step" ).when( stepMeta ).getName();
    doReturn( mock( StepPartitioningMeta.class ) ).when( stepMeta ).getTargetStepPartitioningMeta();
    doReturn( tableOutputMeta ).when( stepMeta ).getStepMetaInterface();

    Database db = mock( Database.class );
    doReturn( mock( Connection.class ) ).when( db ).getConnection();

    tableOutputData = mock( TableOutputData.class );
    tableOutputData.db = db;
    tableOutputData.tableName = "sas";
    tableOutputData.preparedStatements = mock( Map.class );
    tableOutputData.commitCounterMap = mock( Map.class );

    TransMeta transMeta = mock( TransMeta.class );
    doReturn( stepMeta ).when( transMeta ).findStep( anyString() );

    tableOutput = new TableOutput( stepMeta, tableOutputData, 1, transMeta, mock( Trans.class ) );
    tableOutput.setData( tableOutputData );
    tableOutput.setMeta( tableOutputMeta );
    tableOutputSpy = spy( tableOutput );
    doReturn( stepMeta ).when( tableOutputSpy ).getStepMeta();
    doReturn( false ).when( tableOutputSpy ).isRowLevel();
    doReturn( false ).when( tableOutputSpy ).isDebug();
    doNothing().when( tableOutputSpy ).logDetailed( anyString() );
  }

  @Test 
  public void testWriteToTable() throws Exception {
    tableOutputSpy.writeToTable( mock( RowMetaInterface.class ), new Object[]{} );
  }
}
