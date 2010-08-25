(defun clank (input-file output-file)
  ;; can be turned into command with completing filename reads
  (let ((jar-location "clank.jar")
        (compilation-error-regexp-alist-alist
         (cons '(chugga
                 "^| \\(Ambiguity\\)\\(\\[\\(.*\\)\\]\\)? (\\([0-9]+\\),\\([0-9]+\\)) |"
                 3 4 5 2 1)
               compilation-error-regexp-alist-alist))
        (compilation-error-regexp-alist
         (cons 'chugga compilation-error-regexp-alist)))
    (compile (concat "java -jar " jar-location " " output-file "<" input-file))))
