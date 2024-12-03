package org.clevernameinc.itnfinal.forms;

import org.clevernameinc.itnfinal.Staging.ChangeMap;

///
/// @brief
///     Defines functions for Conflict Forms to use
///     This is *mainly* here to decrease coupling between
///     @link ConflictForm and
///     @link ConflictOptionCell
///
public interface ConflictActionManager {

    void mergeSelected(ChangeMap map);
    void deleteSelected(ChangeMap map);
}
