/*******************************************************************************
 * Copyright (c) 2002, 2012 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.events;

import org.eclipse.swt.widgets.Event;


/**
 * Instances of this class are sent as a result of
 * trees being expanded and collapsed.
 *
 * @see TreeListener
 */
public final class TreeEvent extends SelectionEvent {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new instance of this class based on the
   * information in the given untyped event.
   *
   * @param event the untyped event containing the information
   */
  public TreeEvent( Event event ) {
    super( event );
  }

}
