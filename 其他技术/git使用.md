# git分支管理
## 使用场景
当项目中有开发分支(dev)、测试分支(test)和线上部署分支(master)时，如果线上出现紧急bug，需要修复时的做法。

1. 在master分支上创建一个bug修复分支，bug101并切换到这个分支。
2. 修复bug，合并bug101分支到master分支，这时bug就修复好了。
3. 因为master分支也是从dev分支合并的代码所有，dev分支是按说也是存在这个bug的，所以需要手动合并bug101分支到dev分支。不需要把master分支全部合并过来。
4. 使用`cherry-pick`命令把`bug101`分支的bug修改的提交合并到dev分支上。其中`4c805e2`是`bug101`分支`commit`代码是输出的编号，如下：
```shell script
git branch
* dev
  master
$ git cherry-pick 4c805e2
[master 1d4b803] fix bug 101
 1 file changed, 1 insertion(+), 1 deletion(-)
```
`commit bug101`分支
```shell script
$ git add readme.txt 
$ git commit -m "fix bug 101"
[issue-101 4c805e2] fix bug 101
 1 file changed, 1 insertion(+), 1 deletion(-)
```
5. 删除bug101分支，整个bug修复就完成了。

## 参考文章
[廖雪峰Bug分支](https://www.liaoxuefeng.com/wiki/896043488029600/900388704535136)

[git | bug分支](https://www.jianshu.com/p/af74d6374e28)

## 回归指定文件
先执行`git status`查看修改过的文件。
在执行`git checkout -- serivice/url/url_service.js`其中`serivice/url/url_service.js`为需要还原的文件。