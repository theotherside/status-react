(ns status-im.group-settings.screen
  (:require-macros [status-im.utils.views :refer [defview]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [status-im.components.react :refer [view
                                                text-input
                                                text
                                                image
                                                icon
                                                modal
                                                picker
                                                picker-item
                                                scroll-view
                                                touchable-highlight]]
            [status-im.components.toolbar :refer [toolbar]]
            [status-im.components.chat-icon.screen :refer [chat-icon-view-action]]
            [status-im.group-settings.styles.group-settings :as st]
            [status-im.group-settings.views.member :refer [member-view]]
            [status-im.i18n :refer [t]]))

(defn remove-member []
  (dispatch [:remove-participants]))

(defn close-member-menu []
  (dispatch [:set :selected-participants #{}]))

;; TODO not in design
(defview member-menu []
  [{:keys [name] :as participant} [:selected-participant]]
  (when participant
    [modal {:animated       false
            :transparent    false
            :onRequestClose close-member-menu}
     [touchable-highlight {:style    st/modal-container
                           :on-press close-member-menu}
      [view st/modal-inner-container
       [text {:style st/modal-member-name} name]
       [touchable-highlight {:on-press remove-member}
        [text {:style st/modal-remove-text}
         (t :group-settings.remove)]]]]]))

(defview chat-members []
  [members [:current-chat-contacts]]
  [view st/chat-members-container
   (for [member members]
     ^{:key member} [member-view member])])

(defn setting-view [{:keys     [icon-style custom-icon handler title subtitle]
                     icon-name :icon}]
  [touchable-highlight {:on-press handler}
   [view st/setting-row
    [view st/setting-icon-view
     (or custom-icon
         [icon icon-name icon-style])]
    [view st/setting-view
     [text {:style st/setting-title} title]
     (when-let [subtitle subtitle]
       [text {:style st/setting-subtitle}
        subtitle])]]])

(defn close-chat-color-picker []
  (dispatch [:group-settings :show-color-picker false]))

(defn set-chat-color []
  (close-chat-color-picker)
  (dispatch [:set-chat-color]))

;; TODO not in design
(defview chat-color-picker []
  [show-color-picker [:group-settings :show-color-picker]
   new-color [:get :new-chat-color]]
  [modal {:animated       false
          :transparent    false
          :onRequestClose close-chat-color-picker}
   [touchable-highlight {:style    st/modal-container
                         :on-press close-chat-color-picker}
    [view st/modal-color-picker-inner-container
     [picker {:selectedValue new-color
              :onValueChange #(dispatch [:set :new-chat-color %])}
      [picker-item {:label (t :colors.blue) :value "#7099e6"}]
      [picker-item {:label (t :colors.purple) :value "#a187d5"}]
      [picker-item {:label (t :colors.green) :value "green"}]
      [picker-item {:label (t :colors.red) :value "red"}]]
     [touchable-highlight {:on-press set-chat-color}
      [text {:style st/modal-color-picker-save-btn-text}
       (t :group-settings.save)]]]]])

(defview chat-color-icon []
  [chat-color [:chat :color]]
  [view {:style (st/chat-color-icon chat-color)}])

(defn show-chat-color-picker []
  (dispatch [:group-settings :show-color-picker true]))

(defn settings-view []
  (let [settings [{:custom-icon [chat-color-icon]
                   :title       (t :group-settings.change-color)
                   :handler     show-chat-color-picker}
                  ;; TODO not implemented: Notifications
                  (merge {:title    (t :notifications.title)
                          :subtitle (t :not-implemented)
                          :handler  nil}
                         (if true
                           {:icon       :notifications-on
                            :icon-style {:width  16
                                         :height 21}}
                           {:icon       :muted
                            :icon-style {:width  18
                                         :height 21}}))
                  {:icon       :close-gray
                   :icon-style {:width  12
                                :height 12}
                   :title      (t :group-settings.clear-history)
                   ;; TODO show confirmation dialog?
                   :handler    #(dispatch [:clear-history])}
                  {:icon       :bin
                   :icon-style {:width  12
                                :height 18}
                   :title      (t :group-settings.delete-and-leave)
                   ;; TODO show confirmation dialog?
                   :handler    #(dispatch [:leave-group-chat])}]]
    [view st/settings-container
     (for [setting settings]
       ^{:key setting} [setting-view setting])]))

(defview chat-icon []
  [chat-id    [:chat :chat-id]
   group-chat [:chat :group-chat]
   name       [:chat :name]
   color      [:chat :color]]
  [view st/action
   [chat-icon-view-action chat-id group-chat name color false]])

(defn new-group-toolbar []
  [toolbar {:title         (t :group-settings.chat-settings)
            :custom-action [chat-icon]}])

(defn focus []
  (dispatch [:set ::name-input-focused true]))

(defn blur []
  (dispatch [:set ::name-input-focused false]))

(defn save []
  (dispatch [:set-chat-name]))

(defview chat-name []
  [name [:chat :name]
   new-name [:get :new-chat-name]
   focused? [:get ::name-input-focused]]
  [view
   [text {:style st/chat-name-text} (t :chat-name)]
   [view (st/chat-name-value-container focused?)
    [text-input {:style          st/chat-name-value
                 :ref            #(when (and % focused?) (.focus %))
                 :on-change-text #(dispatch [:set :new-chat-name %])
                 :on-focus       focus
                 :on-blur        blur}
     name]
    (if (or focused? (not= name new-name))
      [touchable-highlight {:style    st/chat-name-btn-edit-container
                            :on-press save}
       [view [icon :ok-purple st/add-members-icon]]]
      [touchable-highlight {:style    st/chat-name-btn-edit-container
                            :on-press focus}
       [text {:style st/chat-name-btn-edit-text} (t :group-settings.edit)]])]])

(defview group-settings []
  [show-color-picker [:group-settings :show-color-picker]]
  [view st/group-settings
   [new-group-toolbar]
   [scroll-view st/body
    [chat-name]
    [text {:style st/members-text} (t :members-title)]
    [touchable-highlight {:on-press #(dispatch [:navigate-to :add-participants])}
    ;; TODO add participants view is not in design
     [view st/add-members-container
      [icon :add-gray st/add-members-icon]
      [text {:style st/add-members-text}
       (t :group-settings.add-members)]]]
    [chat-members]
    [text {:style st/settings-text}
     (t :settings.title)]
    [settings-view]]
   (when show-color-picker
     [chat-color-picker])
   [member-menu]])
