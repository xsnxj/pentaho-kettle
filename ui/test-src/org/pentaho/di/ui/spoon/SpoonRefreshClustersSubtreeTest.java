package org.pentaho.di.ui.spoon;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.cluster.ClusterSchema;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.gui.GUIResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Andrey Khayrutdinov
 */
public class SpoonRefreshClustersSubtreeTest {

  private Spoon spoon;

  @Before
  public void setUp() throws Exception {
    spoon = mock( Spoon.class );

    TreeItem mockItem = mock( TreeItem.class );
    when( spoon.createTreeItem( any( TreeItem.class ), anyString(), any( Image.class ) ) ).thenReturn( mockItem );

    doCallRealMethod().when( spoon )
      .refreshClustersSubtree( any( TreeItem.class ), any( TransMeta.class ), any( GUIResource.class ) );
  }


  private void callRefreshWith( TransMeta meta ) {
    spoon.refreshClustersSubtree( mock( TreeItem.class ), meta, mock( GUIResource.class ) );
  }

  private void verifyNumberOfNodesCreated( int times ) {
    verify( spoon, times( times ) ).createTreeItem( any( TreeItem.class ), anyString(), any( Image.class ) );
  }


  @Test
  public void noClusters() {
    TransMeta meta = mock( TransMeta.class );
    when( meta.getClusterSchemas() ).thenReturn( Collections.<ClusterSchema>emptyList() );

    callRefreshWith( meta );
    verifyNumberOfNodesCreated( 1 );
  }

  @Test
  public void severalClustersExist() {
    when( spoon.filterMatch( anyString() ) ).thenReturn( true );
    TransMeta meta = prepareMeta();

    callRefreshWith( meta );
    verifyNumberOfNodesCreated( 4 );
  }

  @Test
  public void onlyOneMatchesFiltering() {
    when( spoon.filterMatch( eq( "2" ) ) ).thenReturn( true );
    TransMeta meta = prepareMeta();

    callRefreshWith( meta );
    verifyNumberOfNodesCreated( 2 );
  }


  private static TransMeta prepareMeta() {
    TransMeta meta = mock( TransMeta.class );
    List<ClusterSchema> schemas = Arrays.asList( createSchema( "1" ), createSchema( "2" ), createSchema( "3" ) );
    when( meta.getClusterSchemas() ).thenReturn( schemas );
    return meta;
  }

  private static ClusterSchema createSchema( String name ) {
    return new ClusterSchema( name, Collections.<SlaveServer>emptyList() );
  }
}
