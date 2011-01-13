/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.graphics.*;
import org.eclipse.swt.internal.graphics.GCOperation.*;
import org.eclipse.swt.widgets.*;


public class GC_Test extends TestCase {

  private Display display;

  public void testConstructorWithNullArgument() {
    try {
      new GC( null );
      fail( "GC( Device ): Must not allow null-argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testInitialValues() {
    GC gc = new GC( display );
    assertEquals( 255, gc.getAlpha() );
    assertEquals( SWT.CAP_FLAT, gc.getLineCap() );
    assertEquals( SWT.JOIN_MITER, gc.getLineJoin() );
    assertEquals( 0, gc.getLineWidth() );
    LineAttributes lineAttributes = gc.getLineAttributes();
    assertEquals( SWT.CAP_FLAT, lineAttributes.cap );
    assertEquals( SWT.JOIN_MITER, lineAttributes.join );
    assertEquals( 0, ( int )lineAttributes.width );
  }
  
  public void testControlGCFont() {
    Shell shell = new Shell( display );
    Control control = new Label( shell, SWT.NONE );
    GC gc = new GC( control );
    assertEquals( control.getFont(), gc.getFont() );
  }

  public void testDisplayGCFont() {
    GC gc = new GC( display );
    assertEquals( display.getSystemFont(), gc.getFont() );
  }

  public void testDisplayGCSetFont() {
    GC gc = new GC( display );
    Font font = createFont();
    gc.setFont( font );
    assertEquals( font, gc.getFont() );
  }

  public void testDisplayGCSetFontWithNullFont() {
    GC gc = new GC( display );
    Font font = createFont();
    gc.setFont( font );
    gc.setFont( null );
    assertEquals( display.getSystemFont(), gc.getFont() );
  }
  
  public void testControlGCSetFont() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    Font font = createFont();
    gc.setFont( font );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetFont operation = ( SetFont )gcOperations[ 0 ];
    assertEquals( font, operation.font );
  }
  
  public void testControlGCSetFontWithNullFont() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    Font font = createFont();
    gc.setFont( font );
    gc.setFont( null );
    assertEquals( display.getSystemFont(), gc.getFont() );
  }
  
  public void testControlGCSetFontWithSameFont() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    Font font = createFont();
    gc.setFont( font );
    IGCAdapter adapter = gc.getGCAdapter();
    adapter.clearGCOperations();
    gc.setFont( font );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }
  
  public void testSetFontWithDisposedFoont() {
    GC gc = new GC( display );
    Font disposedFont = createFont();
    disposedFont.dispose();
    try {
      gc.setFont( disposedFont );
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testDisposedGC() {
    GC gc = new GC( display );
    gc.dispose();
    assertTrue( gc.isDisposed() );
    try {
      gc.setFont( createFont() );
      fail( "setFont not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getFont();
      fail( "getFont not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getCharWidth( 'X' );
      fail( "getCharWidth not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.stringExtent( "" );
      fail( "stringExtent not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.textExtent( "" );
      fail( "textExtent not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getFontMetrics();
      fail( "getFontMetrics not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setBackground( createColor() );
      fail( "setBackground not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getBackground();
      fail( "getBackground not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setForeground( createColor() );
      fail( "setForeground not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getForeground();
      fail( "getForeground not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setAlpha( 123 );
      fail( "setAlpha not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getAlpha();
      fail( "getAlpha not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setLineWidth( 5 );
      fail( "setLineWidth not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getLineWidth();
      fail( "getLineWidth not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setLineCap( SWT.CAP_ROUND );
      fail( "setLineCap not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getLineCap();
      fail( "getLineCap not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setLineJoin( SWT.JOIN_ROUND );
      fail( "setLineJoin not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getLineJoin();
      fail( "getLineJoin not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.setLineAttributes( new LineAttributes( 1, 2, 3 ) );
      fail( "setLineAttributes not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getLineAttributes();
      fail( "getLineAttributes not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawLine( 1, 2, 3, 4 );
      fail( "drawLine not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawPoint( 1, 2 );
      fail( "drawPoint not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawRectangle( 1, 2, 3, 4 );
      fail( "drawRectangle not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillRectangle( 1, 2, 3, 4 );
      fail( "fillRectangle not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillGradientRectangle( 1, 2, 3, 4, true );
      fail( "fillGradientRectangle not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawRoundRectangle( 1, 2, 3, 4, 5, 6 );
      fail( "drawRoundRectangle not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillRoundRectangle( 1, 2, 3, 4, 5, 6 );
      fail( "fillRoundRectangle not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawArc( 1, 2, 3, 4, 5, 6 );
      fail( "drawArc not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillArc( 1, 2, 3, 4, 5, 6 );
      fail( "fillArc not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawOval( 1, 2, 3, 4 );
      fail( "drawOval not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillOval( 1, 2, 3, 4 );
      fail( "fillOval not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawPolygon( new int[] { 1, 2, 3, 4 } );
      fail( "drawPolygon not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.fillPolygon( new int[] { 1, 2, 3, 4 } );
      fail( "fillPolygon not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawPolyline( new int[] { 1, 2, 3, 4 } );
      fail( "drawPolyline not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawText( "text", 1, 1, 0 );
      fail( "drawText not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.drawString( "text", 1, 1, true );
      fail( "drawString not allowed on disposed GC" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_GRAPHIC_DISPOSED, e.code );
    }
    try {
      gc.getClipping();
      fail( "getClipping must not return if GC was disposed" );
    } catch( SWTException e ) {
      // expected
    }
  }

  public void testTextExtentWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.textExtent( null );
      fail( "textExtent must not allow null-argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testTextExtent() {
    GC gc = new GC( display );
    String string = "foo";
    Font systemFont = display.getSystemFont();
    Point gcTextExtent = gc.textExtent( string );
    Point textExtent = Graphics.textExtent( systemFont, string, 0 );
    assertEquals( gcTextExtent, textExtent );
  }

  public void testStringExtent() {
    GC gc = new GC( display );
    String string = "foo";
    Font systemFont = display.getSystemFont();
    Point gcStringExtent = gc.stringExtent( string );
    Point stringExtent = Graphics.stringExtent( systemFont, string );
    assertEquals( gcStringExtent, stringExtent );
  }

  public void testGetCharWidth() {
    GC gc = new GC( display );
    int width = gc.getCharWidth( 'A' );
    assertTrue( width > 0 );
  }

  public void testControlGCBackground() {
    Shell shell = new Shell( display );
    Control control = new Label( shell, SWT.NONE );
    GC gc = new GC( control );
    assertEquals( control.getBackground(), gc.getBackground() );
  }

  public void testDisplayGCBackground() {
    GC gc = new GC( display );
    assertEquals( display.getSystemColor( SWT.COLOR_WHITE ),
                  gc.getBackground() );
  }

  public void testDisplayGCSetBackground() {
    GC gc = new GC( display );
    Color color = createColor();
    gc.setBackground( color );
    assertEquals( color, gc.getBackground() );
  }

  public void testControlGCSetBackground() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    Color color = createColor();
    gc.setBackground( color );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.BACKGROUND, operation.id );
    assertEquals( color, operation.value );
  }

  public void testSetBackgroundNullArgument() {
    GC gc = new GC( display );
    try {
      gc.setBackground( null );
      fail( "null not allowed on setBackground" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetBackgroundWithDisposedColor() {
    GC gc = new GC( display );
    Color color = createColor();
    color.dispose();
    try {
      gc.setBackground( color );
      fail( "disposed color not allowed on setBackground" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testControlGCForeground() {
    Shell shell = new Shell( display );
    Control control = new Label( shell, SWT.NONE );
    GC gc = new GC( control );
    assertEquals( control.getForeground(), gc.getForeground() );
  }

  public void testDisplayGCForeground() {
    GC gc = new GC( display );
    Color black = display.getSystemColor( SWT.COLOR_BLACK );
    assertEquals( black, gc.getForeground() );
  }

  public void testDisplayGCSetForeground() {
    GC gc = new GC( display );
    Color color = createColor();
    gc.setForeground( color );
    assertEquals( color, gc.getForeground() );
  }

  public void testControlGCSetForeground() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    Color color = createColor();
    gc.setForeground( color );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.FOREGROUND, operation.id );
    assertEquals( color, operation.value );
  }
  
  public void testSetForegroundNullArgument() {
    GC gc = new GC( display );
    try {
      gc.setForeground( null );
      fail( "null not allowed on setForeground" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetForegroundWithDisposedColor() {
    GC gc = new GC( display );
    Color color = createColor();
    color.dispose();
    try {
      gc.setForeground( color );
      fail( "disposed color not allowed on setForeground" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testControlGCGetGCAdapterForCanvasWidget() {
    Shell shell = new Shell( display );
    GC gc = new GC( shell );
    IGCAdapter adapter1 = gc.getGCAdapter();
    assertNotNull( adapter1 );
    IGCAdapter adapter2 = gc.getGCAdapter();
    assertSame( adapter2, adapter1 );
  }

  public void testControlGCGetGCAdapterForNonCanvasWidget() {
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.NONE );
    GC gc = new GC( button );
    assertNull( gc.getGCAdapter() );
  }
  
  public void testDisplayGCGetGCAdapter() {
    GC gc = new GC( display );
    assertNull( gc.getGCAdapter() );
  }
  
  public void testDrawOperationWithNonCanvas() {
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.NONE );
    GC gc = new GC( button );
    gc.drawLine( 1, 2, 3, 4 );
    // This test has no assert. Ensures that no NPE is thrown.
  }

  public void testSetAlpha() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.setAlpha( 123 );
    assertEquals( 123, gc.getAlpha() );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.ALPHA, operation.id );
    assertEquals( new Integer( 123 ), operation.value );
  }
  
  public void testSetAlphaWithInvalidValue() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.setAlpha( 777 );
    assertEquals( 255, gc.getAlpha() );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }

  public void testSetLineWidth() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.setLineWidth( 5 );
    assertEquals( 5, gc.getLineWidth() );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.LINE_WIDTH, operation.id );
    assertEquals( new Integer( 5 ), operation.value );
  }
  
  public void testSetLineCap() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.setLineCap( SWT.CAP_ROUND );
    assertEquals( SWT.CAP_ROUND, gc.getLineCap() );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.LINE_CAP, operation.id );
    assertEquals( new Integer( SWT.CAP_ROUND ), operation.value );
    try {
      gc.setLineCap( 500 );
      fail( "value not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetLineJoin() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.setLineJoin( SWT.JOIN_ROUND );
    assertEquals( SWT.JOIN_ROUND, gc.getLineJoin() );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.LINE_JOIN, operation.id );
    assertEquals( new Integer( SWT.JOIN_ROUND ), operation.value );
    try {
      gc.setLineCap( 500 );
      fail( "value not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetLineAttributes() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    LineAttributes attributes
      = new LineAttributes( 5, SWT.CAP_ROUND, SWT.JOIN_BEVEL );
    gc.setLineAttributes( attributes );
    assertEquals( 5, gc.getLineWidth() );
    assertEquals( SWT.CAP_ROUND, gc.getLineCap() );
    assertEquals( SWT.JOIN_BEVEL, gc.getLineJoin() );
    assertEquals( 5, gc.getLineAttributes().width, 0 );
    assertEquals( SWT.CAP_ROUND, gc.getLineAttributes().cap );
    assertEquals( SWT.JOIN_BEVEL, gc.getLineAttributes().join );
    GCOperation[] gcOperations = getGCOperations( gc );
    SetProperty operation = ( SetProperty )gcOperations[ 0 ];
    assertEquals( SetProperty.LINE_WIDTH, operation.id );
    assertEquals( new Integer( 5 ), operation.value );
    operation = ( SetProperty )gcOperations[ 1 ];
    assertEquals( SetProperty.LINE_CAP, operation.id );
    assertEquals( new Integer( SWT.CAP_ROUND ), operation.value );
    operation = ( SetProperty )gcOperations[ 2 ];
    assertEquals( SetProperty.LINE_JOIN, operation.id );
    assertEquals( new Integer( SWT.JOIN_BEVEL ), operation.value );
  }
  
  
  public void testSetLineAttributesWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.setLineAttributes( null );
      fail( "null value not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testDrawLine() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawLine( 1, 2, 3, 4 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawLine operation = ( DrawLine )gcOperations[ 0 ];
    assertEquals( 1, operation.x1 );
    assertEquals( 2, operation.y1 );
    assertEquals( 3, operation.x2 );
    assertEquals( 4, operation.y2 );
  }

  public void testDrawPoint() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawPoint( 1, 2 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawPoint operation = ( DrawPoint )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
  }

  public void testCheckBounds() {
    Rectangle rectangle = GC.checkBounds( 1, 2, 3, 4 );
    assertEquals( 1, rectangle.x );
    assertEquals( 2, rectangle.y );
    assertEquals( 3, rectangle.width );
    assertEquals( 4, rectangle.height );
  }
  
  public void testCheckBoundsWithNegativeWidthAndHeight() {
    Rectangle rectangle = GC.checkBounds( 1, 2, -3, -4 );
    assertEquals( -2, rectangle.x );
    assertEquals( -2, rectangle.y );
    assertEquals( 3, rectangle.width );
    assertEquals( 4, rectangle.height );
  }
  
  public void testDrawRectangle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawRectangle( 1, 2, 3, 4 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertFalse( operation.fill );
  }
  
  public void testControlGCDrawRectangleWithZeroWidthAndHeight() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawRectangle( 1, 2, 0, 0 );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }
  
  public void testDrawRectangeWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.drawRectangle( null );
      fail( "null argument is not allowed on drawRectangle" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testDrawFocus() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawFocus( 1, 2, 3, 4 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertFalse( operation.fill );
  }

  public void testFillRectangle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillRectangle( 1, 2, 3, 4 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertTrue( operation.fill );
  }
  
  public void testFillRectangleWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.fillRectangle( null );
      fail( "null argument is not allowed on fillRectangle" );
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testFillGradientRectangle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillGradientRectangle( 1, 2, 3, 4, true );
    gc.fillGradientRectangle( 5, 6, 7, 8, false );
    GCOperation[] gcOperations = getGCOperations( gc );
    FillGradientRectangle operation 
      = ( FillGradientRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertTrue( operation.vertical );
    assertTrue( operation.fill );
    operation = ( FillGradientRectangle )gcOperations[ 1 ];
    assertEquals( 5, operation.x );
    assertEquals( 6, operation.y );
    assertEquals( 7, operation.width );
    assertEquals( 8, operation.height );
    assertFalse( operation.vertical );
    assertTrue( operation.fill );
  }

  public void testDrawRoundRectangle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawRoundRectangle( 1, 2, 3, 4, 5, 6 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    DrawRoundRectangle operation = ( DrawRoundRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 5, operation.arcWidth );
    assertEquals( 6, operation.arcHeight );
    assertFalse( operation.fill );
  }

  public void testDrawRoundRectangleWithZeroArcWidth() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawRoundRectangle( 1, 2, 3, 4, 0, 6 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertFalse( operation.fill );
  }

  public void testDrawRoundRectangleWithZeroArcHeight() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawRoundRectangle( 1, 2, 3, 4, 5, 0 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertFalse( operation.fill );
  }
  
  public void testFillRoundRectangle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillRoundRectangle( 1, 2, 3, 4, 5, 6 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRoundRectangle operation = ( DrawRoundRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 5, operation.arcWidth );
    assertEquals( 6, operation.arcHeight );
    assertTrue( operation.fill );
  }
  
  public void testFillRoundRectangleWithZeroArcWidth() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillRoundRectangle( 1, 2, 3, 4, 0, 6 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertTrue( operation.fill );
  }

  public void testFillRoundRectangleWithZeroArcHeight() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillRoundRectangle( 1, 2, 3, 4, 5, 0 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawRectangle operation = ( DrawRectangle )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertTrue( operation.fill );
  }
  
  public void testDrawArc() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawArc( 1, 2, 3, 4, 5, 6 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    DrawArc operation = ( DrawArc )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 5, operation.startAngle );
    assertEquals( 6, operation.arcAngle );
    assertFalse( operation.fill );
  }

  public void testDrawArcWithZeroWidth() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawArc( 1, 2, 0, 5, 5, 5 );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }

  public void testDrawArcWithZeroHeight() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawArc( 1, 2, 3, 0, 5, 5 );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }
  
  public void testDrawArcWithZeroArcAngle() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawArc( 1, 2, 3, 4, 5, 0 );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }
  
  public void testFillArc() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillArc( 1, 2, 3, 4, 5, 6 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    DrawArc operation = ( DrawArc )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 5, operation.startAngle );
    assertEquals( 6, operation.arcAngle );
    adapter.clearGCOperations();

    gc.fillArc( 1, 2, -3, -4, 5, 6 );
    gcOperations = adapter.getGCOperations();
    operation = ( DrawArc )gcOperations[ 0 ];
    assertEquals( -2, operation.x );
    assertEquals( -2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 5, operation.startAngle );
    assertEquals( 6, operation.arcAngle );
    assertTrue( operation.fill );
    adapter.clearGCOperations();

    gc.fillArc( 1, 2, 0, 4, 5, 6 );
    gcOperations = adapter.getGCOperations();
    assertEquals( 0, gcOperations.length );

    gc.fillArc( 1, 2, 3, 0, 5, 6 );
    gcOperations = adapter.getGCOperations();
    assertEquals( 0, gcOperations.length );

    gc.fillArc( 1, 2, 3, 4, 5, 0 );
    gcOperations = adapter.getGCOperations();
    assertEquals( 0, gcOperations.length );
  }

  public void testDrawOval() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawOval( 1, 2, 3, 4 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    DrawArc operation = ( DrawArc )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 0, operation.startAngle );
    assertEquals( 360, operation.arcAngle );
    assertFalse( operation.fill );
  }

  public void testDrawOvalWithZeroWidthAndHeight() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawOval( 1, 2, 0, 0 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    assertEquals( 0, gcOperations.length );
  }
  
  public void testFillOval() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.fillOval( 1, 2, 3, 4 );
    IGCAdapter adapter = gc.getGCAdapter();
    GCOperation[] gcOperations = adapter.getGCOperations();
    DrawArc operation = ( DrawArc )gcOperations[ 0 ];
    assertEquals( 1, operation.x );
    assertEquals( 2, operation.y );
    assertEquals( 3, operation.width );
    assertEquals( 4, operation.height );
    assertEquals( 0, operation.startAngle );
    assertEquals( 360, operation.arcAngle );
    assertTrue( operation.fill );
    adapter.clearGCOperations();
  }

  public void testDrawPolygon() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    int[] pointArray = new int[] { 1, 2, 3, 4 };
    gc.drawPolygon( pointArray );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawPolyline operation = ( DrawPolyline )gcOperations[ 0 ];
    assertTrue( Arrays.equals( pointArray, operation.points ) );
    assertTrue( operation.close );
    assertFalse( operation.fill );
  }

  public void testDrawPolygonWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.drawPolygon( null );
    } catch( IllegalArgumentException expected ) {
    }
  }
  
  public void testFillPolygon() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    int[] pointArray = new int[] { 1, 2, 3, 4 };
    gc.fillPolygon( pointArray );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawPolyline operation = ( DrawPolyline )gcOperations[ 0 ];
    assertTrue( Arrays.equals( pointArray, operation.points ) );
    assertTrue( operation.close );
    assertTrue( operation.fill );
  }

  public void testFillPolygonWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.fillPolygon( null );
    } catch( IllegalArgumentException expected ) {
    }
  }
  
  public void testDrawPolyline() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    int[] pointArray = new int[] { 1, 2, 3, 4 };
    gc.drawPolyline( pointArray );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawPolyline operation = ( DrawPolyline )gcOperations[ 0 ];
    assertTrue( Arrays.equals( pointArray, operation.points ) );
    assertFalse( operation.close );
    assertFalse( operation.fill );
  }

  public void testDrawPolylineWithNullArgument() {
    GC gc = new GC( display );
    try {
      gc.drawPolyline( null );
    } catch( IllegalArgumentException expected ) {
    }
  }
  
  public void testDrawText() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawText( "text", 10, 10, SWT.DRAW_TRANSPARENT );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawText operation = ( DrawText )gcOperations[ 0 ];
    assertEquals( "text", operation.text );
    assertEquals( 10, operation.x );
    assertEquals( 10, operation.y );
    assertEquals( SWT.DRAW_TRANSPARENT, operation.flags );
  }

  public void testDrawTextWithNullString() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    try {
      gc.drawText( null, 10, 10, SWT.DRAW_TRANSPARENT );
      fail( "null argument is not allowed on drawText" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }
  
  public void testDrawTextWithEmptyString() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawText( "", 10, 10, SWT.DRAW_TRANSPARENT );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }

  public void testDrawString() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawString( "text", 10, 10, true );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawText operation = ( DrawText )gcOperations[ 0 ];
    assertEquals( "text", operation.text );
    assertEquals( 10, operation.x );
    assertEquals( 10, operation.y );
    assertEquals( SWT.DRAW_TRANSPARENT, operation.flags );
  }

  public void testDrawStringWithNullString() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    try {
      gc.drawString( null, 10, 10, true );
      fail( "null argument is not allowed on drawText" );
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testDrawStringWithEmptyString() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    gc.drawString( "", 10, 10, false );
    GCOperation[] gcOperations = getGCOperations( gc );
    assertEquals( 0, gcOperations.length );
  }
  
  public void testDrawImageWithNullImage() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    try {
      gc.drawImage( null, 1, 2 );
      fail( "null argument is not allowed on drawImage" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testDrawImageWithDisposedImage() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    Image disposedImage = createImage();
    disposedImage.dispose();
    try {
      gc.drawImage( disposedImage, 1, 2 );
      fail( "drawImage must not allow disposed image" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testDrawImage() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    Image image = display.getSystemImage( SWT.ICON_INFORMATION );
    gc.drawImage( image, 1, 2 );
    GCOperation[] gcOperations = getGCOperations( gc );
    DrawImage operation = ( DrawImage )gcOperations[ 0 ];
    assertSame( image, operation.image );
    assertEquals( 0, operation.srcX );
    assertEquals( 0, operation.srcY );
    assertEquals( -1, operation.srcWidth );
    assertEquals( -1, operation.srcHeight );
    assertEquals( 1, operation.destX );
    assertEquals( 2, operation.destY );
    assertEquals( -1, operation.destWidth );
    assertEquals( -1, operation.destHeight );
    assertTrue( operation.simple );
    gc.drawImage( image, 1, 2, 3, 4, 5, 6, 7, 8 );
    gcOperations = getGCOperations( gc );
    operation = ( DrawImage )gcOperations[ 1 ];
    assertSame( image, operation.image );
    assertEquals( 1, operation.srcX );
    assertEquals( 2, operation.srcY );
    assertEquals( 3, operation.srcWidth );
    assertEquals( 4, operation.srcHeight );
    assertEquals( 5, operation.destX );
    assertEquals( 6, operation.destY );
    assertEquals( 7, operation.destWidth );
    assertEquals( 8, operation.destHeight );
    assertFalse( operation.simple );
  }
  
  public void testDrawImageWithInvalidSourceRegion() {
    Control control = new Shell( display );
    GC gc = new GC( control );
    Image image = display.getSystemImage( SWT.ICON_INFORMATION );
    assertTrue( image.getBounds().width < 40 );
    assertTrue( image.getBounds().height < 40 );
    try {
      gc.drawImage( image, 10, 0, 50, 16, 0, 0, 100, 100 );
      fail( "srcWidth larger than srcX + image.width is not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      gc.drawImage( image, 0, 10, 16, 50, 0, 0, 100, 100 );
      fail( "srcHeight larger than srcY + image.height is not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testControlGCGetClipping() {
    Shell shell = new Shell( display );
    Canvas canvas = new Canvas( shell, SWT.NONE );
    canvas.setSize( 100, 100 );
    GC gc = new GC( canvas );
    Rectangle clipping = gc.getClipping();
    assertEquals( new Rectangle( 0, 0, 100, 100), clipping );
  }

  public void testDisplayGCGetClipping() {
    GC gc = new GC( display );
    Rectangle clipping = gc.getClipping();
    assertEquals( display.getBounds(), clipping );
  }

  public void testStyle() {
    GC gc = new GC( display, SWT.NONE );
    assertEquals( SWT.LEFT_TO_RIGHT, gc.getStyle() );
    gc = new GC( display, SWT.LEFT_TO_RIGHT );
    assertEquals( SWT.LEFT_TO_RIGHT, gc.getStyle() );
    gc = new GC( display, SWT.PUSH );
    assertEquals( SWT.LEFT_TO_RIGHT, gc.getStyle() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    display = new Display();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  private static GCOperation[] getGCOperations( final GC gc ) {
    GCAdapter adapter = gc.getGCAdapter();
    return adapter.getGCOperations();
  }

  private Image createImage() {
    ClassLoader loader = Fixture.class.getClassLoader();
    InputStream stream = loader.getResourceAsStream( Fixture.IMAGE1 );
    return new Image( display, stream );
  }

  private Color createColor() {
    return new Color( display, 1, 2, 3 );
  }

  private Font createFont() {
    return new Font( display, "font-name", 11, SWT.NORMAL );
  }
}
