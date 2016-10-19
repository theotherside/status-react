(ns status-im.chat.handlers.console
  (:require [re-frame.core :refer [dispatch]]
            [status-im.utils.handlers :refer [register-handler] :as u]
            [status-im.constants :refer [console-chat-id]]))

(def console-commands
  {:password
   (fn [params parameters]
     (dispatch [:create-account (get params "password")])
     (dispatch [:prepare-command! parameters]))

   :phone
   (fn [params parameters]
     (dispatch [:sign-up (get params "phone") parameters]))

   :confirmation-code
   (fn [params parameters]
     (dispatch [:sign-up-confirm (get params "code") parameters]))})

(register-handler :invoke-console-command-handler!
  (u/side-effect!
    (fn [_ [_ {:keys [staged-command] :as   parameters}]]
      (let [{:keys [command params]} staged-command
            {:keys [name]} command]
        ((console-commands (keyword name)) params parameters)))))
