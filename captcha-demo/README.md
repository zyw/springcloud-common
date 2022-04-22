## miitvip-captcha的后端例子
### php 的示例：

> 初始化 init 时返回 key 值，key 值生成规则自行定义
> 校验 verification 时，匹配 key 值

```php
/**
 * 初始化.
 * @param Request $request
 * @return JsonResponse
 */
public function init(Request $request): JsonResponse
{
    session_start();
    $key = $this->create($request);
    $_SESSION['mi_captcha_server'] = 1;
    return $this->success([
        'id' => $this->captcha_id,
        'key' => $key
    ]);
}


/**
 * 校验.
 * @param Request $request
 * @return JsonResponse
 */
public function verification(Request $request): JsonResponse
{
    session_start();
    $key = strip_tags(trim($request->input('key')));
    if ($this->create($request) == $key) {
        if ($_SESSION['mi_captcha_server'] == 1) {
            $uuid = uuid(true);
            Redis::set($uuid, 1, 'EX', 3600);
            return $this->success(['uuid' => $uuid]);
        }
    }
    return $this->error('E010021');
}



/**
 * 生成加密串.
 * @param Request $request
 * @return string
 */
private function create(Request $request): string
{
    $ip = md5($request->getClientIp());
    $cid = md5($this->captcha_id . config('app.salt') . $ip);
    return md5($cid . $this->private_key);
}
```